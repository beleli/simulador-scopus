package br.com.scopus.simulador.business.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.jerimum.fw.service.impl.AbstractCrudService;
import br.com.scopus.simulador.business.i18n.I18nKeys;
import br.com.scopus.simulador.business.service.ProjectService;
import br.com.scopus.simulador.business.service.ProjectUserService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ProjectDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PageUtil;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.ProjectRepository;
import br.com.scopus.simulador.repository.entity.Project;

/**
 * Classe de servico para projetos.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Service
public class ProjectServiceImpl extends AbstractCrudService<ProjectDto, Project> implements ProjectService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectUserService projectUserService;

    @Override
    protected JpaCrudRepository<Project, Long> getRepository() {
        return this.projectRepository;
    }

    @Override
    protected ProjectDto buildDtoFromEntity(Project entity) {
        List<ComboBoxDto> users = new ArrayList<ComboBoxDto>();
        if (entity.getId() != null) {
            users = this.projectUserService.findUserList(entity.getId());
        }
        ProjectDto dto = new ProjectDto(entity.getId(), entity.getCode(), entity.getDescription(),
            entity.getBeginDate(), entity.getEndDate(), users);

        return dto;
    }

    @Override
    protected Project buildEntityFromDto(ProjectDto dto) {
        Project entity = new Project(dto.getId(), dto.getCode(), dto.getDescription(), formatDate(dto.getBeginDate()),
            formatDate(dto.getEndDate()));
        return entity;
    }

    private Date formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public PageDto<ProjectDto> findByProject(PagedSearch<ProjectDto> pagedSearch) {
        ProjectDto dto = pagedSearch.getItem();

        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        if (isNullOrEmpty(dto.getCode()))
            dto.setCode(null);
        if (isNullOrEmpty(dto.getDescription()))
            dto.setDescription(null);

        Page<Project> page = this.projectRepository.findByCodeAndDescription(dto.getCode(), dto.getDescription(),
            PageUtil.createPageRequest(pagedSearch));

        return new PageDto<>(buildDtoFromEntity(page.getContent()), page.getTotalElements());
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.trim().equals("");
    }

    @Override
    @Transactional(readOnly = false)
    public ProjectDto save(ProjectDto dto) throws ValidationException, ServiceException {
        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        Project project = projectRepository.findByCode(dto.getCode());
        if (project != null && !project.getId().equals(dto.getId())) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ProjetoJaCadastrado.getKey());
            throw new ValidationException(ReturnCode.PROJECT_REGISTERED.getCode(), msg);
        }

        if (dto.getEndDate().before(dto.getBeginDate())) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.DataInicialMaiorDataFinal.getKey());
            throw new ValidationException(ReturnCode.INVALID_DATE_INITIAL_FINAL.getCode(), msg);
        }

        Project entity = projectRepository.save(buildEntityFromDto(dto));
        if (dto.getId() == null)
            dto.setId(entity.getId());

        if (dto.getUsers() == null)
            dto.setUsers(new ArrayList<ComboBoxDto>());

        this.projectUserService.saveUserList(dto.getUsers(), dto.getId());
        return dto;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
        this.projectUserService.deleteByProject(id);
        this.projectRepository.delete(id);
    }

    @Override
    public List<ComboBoxDto> getComboBoxActiveProjects() {
        List<Project> projectList = this.projectRepository.findActiveProjects(formatDate(new Date()));
        List<ComboBoxDto> dtos = new ArrayList<>();
        for (Project p : projectList) {
            dtos.add(new ComboBoxDto(p.getId(), p.getCode()));
        }
        return dtos;
    }

    @Override
    public boolean isActiveProject(Long projectId) {
        Project entity = projectRepository.findOne(projectId);
        Date date = new Date();
        return date.after(entity.getBeginDate()) && date.before(entity.getEndDate());
    }
}
