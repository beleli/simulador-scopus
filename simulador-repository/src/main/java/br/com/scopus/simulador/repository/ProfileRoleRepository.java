package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.ProfileRole;
import br.com.scopus.simulador.repository.entity.enums.Profile;

/**
 * Repositorio para a entidade ProfileRole.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Jefferson Borges - jbsantos@scopus.com.br
 * @since 1.0
 */
@Repository
public interface ProfileRoleRepository extends JpaCrudRepository<ProfileRole, Long>{
    
    List<ProfileRole> findByProfile(Profile profile);
}
