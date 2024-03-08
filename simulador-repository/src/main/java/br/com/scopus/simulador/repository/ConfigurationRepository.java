package br.com.scopus.simulador.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.Configuration;
import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;

/**
 * Repositorio para a entidade Configuration.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface ConfigurationRepository extends JpaCrudRepository<Configuration, Long> {

    /**
     * Retorna as configurações de acordo com o nome e o tipo de transação.
     * 
     * @param transactionType
     * @param name
     * @param pageable
     * @return {@link Project}
     */
    @Query("SELECT c FROM Configuration c"
         + " WHERE (:transactionType IS NULL OR c.transactionType = :transactionType)"
         + "   AND (:name IS NULL OR c.name LIKE '%' + :name + '%')"
         + " ORDER BY c.name ASC")
    Page<Configuration> searchByTransactionTypeAndNameOrderByName(@Param("transactionType") TransactionType transactionType,
        @Param("name") String name, Pageable pageable);

}
