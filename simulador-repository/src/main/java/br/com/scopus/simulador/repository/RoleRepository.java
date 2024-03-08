package br.com.scopus.simulador.repository;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.Role;

/**
 * Repositorio para a entidade Role.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Repository
public interface RoleRepository extends JpaCrudRepository<Role, Long> {

}
