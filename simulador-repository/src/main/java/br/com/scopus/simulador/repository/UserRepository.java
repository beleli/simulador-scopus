package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.User;

/**
 * Repositorio para a entidade User.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaCrudRepository<User, Long> {

    /**
     * Retorna o usuario que possui o email informado como parametro.
     * 
     * @param email
     * @return {@link User}
     */
    User findByEmail(String email);
    
    /**
     * Retorna o usuario informado como parametro.
     * 
     * @param id
     * @return User
     */
    User findById(Long id);
    
    /**
     * Busca os usuários de acordo com o nome passado como parametro
     * @param name
     * @return List<User>
     */
    @Query("SELECT u FROM User u"
         + " WHERE u.name LIKE '%' + :name + '%'"
         + "   AND u.enabled = true"
         + " ORDER BY u.name")
    List<User> searchByName(@Param("name") String name);
    
    /**
     * Busca os usuários de acordo com o nome passado como parametro
     * @param name
     * @return List<User>
     */
    List<User> findByNameIgnoreCaseContainingAndEnabledTrue(String name);
    
    /**
     * Consulta paginada de usuários
     * 
     * @param name
     * @param email
     * @param pageable
     * @return Page<User>
     */
    Page<User> findByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndEnabled(String name, String email,
        boolean enabled, Pageable pageable);

    /**
     * Consulta a quantidade de e-mails desprezando o de um usuário por seu ID.
     * 
     * @param name
     * @return List<Object[]>
     */
    @Query(" SELECT count(u) FROM User u WHERE upper(u.email) = upper(:email) and u.id != :id")
    Integer findQuantityEmails(@Param("email") String email, @Param("id") Long id);
    
    /**
     * Retorna a lista de usuários de acordo com o email e nome
     * 
     * @param email
     * @param name
     * @param pageable
     * @return Page<User>
     */
    @Query("SELECT u FROM User u"
         + " WHERE (:email IS NULL OR u.email LIKE '%' + :email + '%')"
         + "   AND (:name IS NULL OR u.name LIKE '%' + :name + '%')"
         + " ORDER BY u.name ASC")
    Page<User> findByEmailAndNameOrderByName(@Param("email") String email, @Param("name") String name, Pageable pageable);
    
}
