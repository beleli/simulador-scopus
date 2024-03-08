package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.TransactionParent;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;

/**
 * Repositorio para a entidade TransactionParent.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface TransactionParentRepository extends JpaCrudRepository<TransactionParent, Long> {

    /**
     * Busca todas as transações de acordo com a identificação.
     * 
     * @param identification
     * @param pageable
     * @return lista de transações
     */
    @Query("SELECT v FROM TransactionParent v"
         + " WHERE :identification IS NULL OR v.identification LIKE '%' + :identification + '%'"
         + " ORDER BY v.identification")
    Page<TransactionParent> findByIdentificationOrderByIdentification(@Param("identification") String identification,
        Pageable pageable);

    /**
     * Busca a lista de transação que podem ser herdadas
     * 
     * @return
     */
    List<TransactionParent> findByParentTrueOrderByIdentification();

    /**
     * Busca os transações de acordo com o nome passado como parametro
     * 
     * @param name
     * @return List<Transaction>
     */
    @Query("SELECT v FROM TransactionParent v"
         + " WHERE v.identification LIKE '%' + :identification + '%'"
         + " ORDER BY v.description")
    List<TransactionParent> searchByIdentification(@Param("identification") String identification);

    /**
     * Busca os transações de acordo com o nome passado como parametro
     * 
     * @param name
     * @return List<Transaction>
     */
    @Query("SELECT v FROM TransactionParent v"
         + " WHERE v.identification LIKE '%' + :identification + '%'"
         + "   AND v.pk.layoutOutputTransactionId != 0"
         + " ORDER BY v.identification")
    List<TransactionParent> searchTranactionsScenarioByIdentification(@Param("identification") String identification);

    /**
     * Busca a transação de acordo com o layout de saída
     * 
     * @param layoutOutputTransactionId
     * @return TransactionParent
     */
    TransactionParent findByPkLayoutOutputTransactionId(Long layoutOutputTransactionId);
    
    /**
     * Busca as transações roteadores para o tipo passado como paremetro
     * 
     * @param routerType
     * @return List<TransactionParent>
     */
    @Query("SELECT v FROM TransactionParent v"
        + " WHERE v.routerType = :routerType"
        + "   AND v.pk.layoutOutputTransactionId != 0"
        + " ORDER BY v.identification")
    List<TransactionParent> findByRouterType(@Param("routerType") TransactionType routerType);

}
