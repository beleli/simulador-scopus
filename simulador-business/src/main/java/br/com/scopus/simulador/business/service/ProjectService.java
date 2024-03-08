package br.com.scopus.simulador.business.service;

import java.util.List;

import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.service.CrudService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ProjectDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.entity.Project;

/**
 * Interface de servico para projetos.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public interface ProjectService extends CrudService<ProjectDto, Project> {

    /**
     * Realiza a busca paginada de projetos
     * 
     * @param {@link PagedSearch}<{@link ProjectDto}>
     * @return {@link PageDto}<{@link ProjectDto}>
     * @throws ValidationException
     * @throws ServiceException
     */
    PageDto<ProjectDto> findByProject(PagedSearch<ProjectDto> pagedSearch);


    /**
     * Realiza a busca paginada de projetos
     * 
     * @param {@link dto}<{@link ProjectDto}>
     * @return {@link dto}<{@link ProjectDto}>
     * @throws ValidationException
     * @throws ServiceException
     */
    ProjectDto save(ProjectDto dto) throws ValidationException, ServiceException;


    /**
     * Realiza a busca paginada de projetos
     * 
     * @param Long id
     * @throws ValidationException
     * @throws ServiceException
     */
    void delete(Long id);

    /**
     * Busca a lista de projetos ativos.
     * 
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> getComboBoxActiveProjects();

    /**
     * Valida se o projeto está ativo.
     * 
     * @param projectId
     * @return
     */
    boolean isActiveProject(Long projectId);
}
