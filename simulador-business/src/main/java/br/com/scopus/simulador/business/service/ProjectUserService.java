package br.com.scopus.simulador.business.service;

import java.util.List;

import br.com.jerimum.fw.service.CrudService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.ProjectUserDto;
import br.com.scopus.simulador.repository.entity.ProjectUser;

public interface ProjectUserService extends CrudService<ProjectUserDto, ProjectUser> {

    /**
     * Salva a lista de usuários de acordo com o projeto indicado.
     * 
     * @param dtos
     * @param projetcId
     */
    void saveUserList(List<ComboBoxDto> dtos, Long projetcId);

    /**
     * Busca de lista de usuários de acordo com o projeto indicado.
     * @param projectId
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> findUserList(Long projectId);

    /**
     * Deleta a entidade
     * 
     * @param projectId
     */
    void deleteByProject(Long projectId);

    /**
     * Valida se o projeto ainda está ativo e se o usuário é válido.
     * @param projectId
     * @param userId
     * @return
     */
    boolean isValidUserPermission(Long projectId, Long userId);
    
}
