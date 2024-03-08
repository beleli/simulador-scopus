package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.TestScenario;

/**
 * Repositorio para a entidade Transaction.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface TestScenarioRepository extends JpaCrudRepository<TestScenario, Long> {

    TestScenario findByDescription(String description);

    /**
     * Retorna todos os cenarios de teste.
     * 
     * @param pageable
     * @return {@link Project}
     */
    @Query("SELECT t FROM TestScenario t"
         + " WHERE (:transacationId IS NULL OR t.transaction.id = :transacationId)"
         + "   AND (:description IS NULL OR t.description LIKE '%' + :description + '%')"
         + " ORDER BY t.description")
    Page<TestScenario> findByTransacationIdAndDescription(@Param("transacationId") Long transacationId,
        @Param("description") String description, Pageable pageable);

    /**
     * Busca a lista de cenários de teste por transação pai.
     * 
     * @param transactionId
     * @return List<TestScenario>
     */
    List<TestScenario> findByTransactionIdOrderByDescription(Long transactionId);
}
