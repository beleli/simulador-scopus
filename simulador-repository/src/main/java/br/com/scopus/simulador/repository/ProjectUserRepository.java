package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.ProjectUser;

/**
 * Repositorio para a entidade ProjectUser.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface ProjectUserRepository extends JpaCrudRepository<ProjectUser, Long> {

    /**
     * Retorna a lisa de usuários por projeto.
     * 
     * @param projectId
     * @return List<ProjectUser>
     */
    List<ProjectUser> findByProjectIdOrderByUserName(Long projectId);

    /**
     * Retorna o usuarios de acordo com os parametros.
     * 
     * @param projectId
     * @return List<ProjectUser>
     */
    ProjectUser findByProjectIdAndUserId(Long projectId, Long userId);

}
