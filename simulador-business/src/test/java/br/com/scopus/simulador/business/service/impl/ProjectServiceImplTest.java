package br.com.scopus.simulador.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.scopus.simulador.business.JUnitBusinessTestContext;
import br.com.scopus.simulador.business.i18n.I18nKeys;
import br.com.scopus.simulador.business.service.ProjectService;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ProjectDto;
import br.com.scopus.simulador.dto.template.PagedSearchTemplate;
import br.com.scopus.simulador.dto.template.ProjectDtoTemplate;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.ProjectRepository;
import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.User;
import br.com.scopus.simulador.repository.entity.template.ProjectTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Testes unitario para a classe de servico ProjectService.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JUnitBusinessTestContext.class })
public class ProjectServiceImplTest extends JUnitBusinessTestContext {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(this.projectRepository);
        FixtureFactoryLoader.loadTemplates(ProjectTemplate.class.getPackage().getName());
        FixtureFactoryLoader.loadTemplates(ProjectDtoTemplate.class.getPackage().getName());
    }

    @Test
    public void testCrud() throws Exception {

        /* customiza o comportamento dos mocks */
        Mockito.reset(this.projectRepository);

        Project entity = Fixture.from(Project.class).gimme(ProjectTemplate.NOVO_COM_ID);
        Mockito.when(this.projectRepository.save(Mockito.any(Project.class))).thenReturn(entity);
        Mockito.when(this.projectRepository.findOne(Mockito.any(Long.class))).thenReturn(entity);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos
         */
        ProjectDto dto = Fixture.from(ProjectDto.class).gimme(ProjectDtoTemplate.NOVO);
        assertNotNull(dto);
        assertNull(dto.getId());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos
         */
        dto = this.projectService.insertDto(dto);
        assertNotNull(dto);
        assertNotNull(dto.getId());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ser
         * implementado para o DTO)
         */
        ProjectDto testDto = this.projectService.getDtoById(dto.getId());
        assertEquals(testDto, dto);

        /* altera o dto e verifica se a alteracao foi persistida */
        dto = new ProjectDto(dto.getId(), dto.getCode(), RandomStringUtils.randomAlphanumeric(10), dto.getBeginDate(),
            dto.getEndDate(), null);
        dto = this.projectService.updateDto(dto);
        testDto = this.projectService.getDtoById(dto.getId());
        assertEquals(testDto, dto);

        try {

            /* remove o dto e verifica se ele foi removido */
            this.projectService.deleteDtoById(dto.getId());

            /* customiza o comportamento dos mocks */
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.NenhumRegistroEncontrado.getKey(),
                User.class.getSimpleName(), entity.getId());
            Mockito.when(this.projectRepository.findOne(Mockito.any(Long.class)))
                .thenThrow(new EmptyResultDataAccessException(msg, 1));

            dto = this.projectService.getDtoById(dto.getId());
            Assert.fail();

        } catch (EmptyResultDataAccessException e) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.NenhumRegistroEncontrado.getKey(),
                User.class.getSimpleName(), testDto.getId());
            Assert.assertThat(e.getMessage(), CoreMatchers.containsString(msg));
        }
    }

    @Test
    public void test_findByProject() throws Exception {
        /* customiza o comportamento dos mocks */
        Mockito.reset(this.projectRepository);

        List<Project> listaRetorno = new ArrayList<>(Fixture.from(Project.class).gimme(5, ProjectTemplate.NOVO_COM_ID));
        Pageable pagina = new PageRequest(0, 5);
        Page<Project> page = new PageImpl<>(listaRetorno, pagina, 5);

        Mockito.when(this.projectRepository.findByCodeAndDescription(Mockito.any(String.class),
            Mockito.any(String.class), Mockito.any(Pageable.class))).thenReturn(page);

        PagedSearch<ProjectDto> dto = Fixture.from(PagedSearch.class).gimme(PagedSearchTemplate.PROJECT);

        PageDto<ProjectDto> lista = this.projectService.findByProject(dto);
        Assert.assertEquals(lista.getTotalRegister(), 5L);
    }

    @Test
    public void test_findByProjects_null() throws Exception {
        /* customiza o comportamento dos mocks */
        Mockito.reset(this.projectRepository);
        Mockito.when(this.projectRepository.findByCodeAndDescription(Mockito.any(String.class),
            Mockito.any(String.class), Mockito.any(Pageable.class)))
            .thenReturn(new PageImpl<Project>(new ArrayList<Project>()));

        PagedSearch<ProjectDto> dto = Fixture.from(PagedSearch.class).gimme(PagedSearchTemplate.PROJECT);
        PageDto<ProjectDto> lista = this.projectService.findByProject(dto);
        Assert.assertEquals(lista.getTotalRegister(), 0L);
    }
    
    @Test
    public void test_findByProjects_InvalidParamters() throws Exception {
        PagedSearch<ProjectDto> dto = Fixture.from(PagedSearch.class).gimme(PagedSearchTemplate.PROJECT);
        dto.setItem(null);

        try {
            this.projectService.findByProject(dto);
            Assert.fail();

        } catch (ValidationException e) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            Assert.assertThat(e.getMessage(), CoreMatchers.containsString(msg));
        }
    }

}
