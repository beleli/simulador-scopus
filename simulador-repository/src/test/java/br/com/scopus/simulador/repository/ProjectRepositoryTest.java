package br.com.scopus.simulador.repository;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.template.ProjectTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio projectRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class ProjectRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient ProjectRepository projectRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(ProjectTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        Project project1 = Fixture.from(Project.class).gimme(ProjectTemplate.NOVO_COM_ID);
        Project project2 = new Project(project1.getPK());
        Project project3 = new Project(project1.getId(), project1.getCode(), project1.getDescription(),
            project1.getBeginDate(), project1.getEndDate());
        Assert.assertEquals(project1, project2);
        Assert.assertEquals(project2, project3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {

        Project entity = Fixture.from(Project.class).gimme(ProjectTemplate.NOVO);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.projectRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getCode(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        Project testEntity = this.projectRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setCode(RandomStringUtils.randomAlphanumeric(10));
        entity = this.projectRepository.save(entity);
        testEntity = this.projectRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.projectRepository.delete(entity);
        entity = this.projectRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }

    @Test
    @Ignore
    public void test_searchByCodeAndDescription() {

        Project project = Fixture.from(Project.class).gimme(ProjectTemplate.NOVO);
        this.projectRepository.save(project);

        Pageable pageable = new PageRequest(0, 5);
        Page<Project> lista = this.projectRepository.findByCodeAndDescription(project.getCode(), project.getDescription(), pageable);
        Assert.assertEquals(lista.getContent().size(), 1);
    }

}
