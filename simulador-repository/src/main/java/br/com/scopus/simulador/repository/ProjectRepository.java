package br.com.scopus.simulador.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.Project;

/**
 * Repositorio para a entidade Project.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface ProjectRepository extends JpaCrudRepository<Project, Long> {

    
    /**
     * Retorna os projetos que possuem a descrição informado como parametro.
     * 
     * @param description
     * @param pageable
     * @return {@link Project}
     */
    @Query("SELECT p FROM Project p"
        + " WHERE (:code IS NULL OR p.code LIKE '%' + :code + '%')"
        + "   AND (:description IS NULL OR p.description LIKE '%' + :description + '%')"
        + " ORDER BY p.code ASC")
    Page<Project> findByCodeAndDescription(@Param("code") String code, @Param("description") String description,
        Pageable pageable);
    
    /**
     * Busca os projetos ativos para a data informada como parametro.
     * @param date
     * @return List<Project>
     */
    @Query("SELECT p FROM Project p"
         + " WHERE :date BETWEEN p.beginDate AND p.endDate")
    List<Project> findActiveProjects(@Param("date") Date date);
    
    /**
     * Busca o projeto pelo código informado como parametro.
     * @param code
     * @return Project
     */
    Project findByCode(String code);
    
}
