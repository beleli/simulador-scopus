package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;

/**
 * Repositorio para a entidade Transaction.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface TransactionRepository extends JpaCrudRepository<Transaction, Long> {

    /**
     * Retorna todas as transacoes.
     * 
     * @param pageable
     * @return {@link Project}
     */
    @Query("select t from Transaction t order by t.identification")
    List<Transaction> findAllOrderByIdentification();
    
    /**
     * Retorna todas as transacoes pai.
     * 
     * @param pageable
     * @return {@link Project}
     */
    List<Transaction> findByParentTrueOrderByIdentification();

    /**
     * Retorna todas as transações de acordo com a identificação.
     * 
     * @param pageable
     * @return {@link Project}
     */
    Page<Transaction> findByIdIgnoreCaseContainingOrderById(String identification, Pageable pageable);

    Page<Transaction> findByIdentificationIgnoreCaseContainingOrderByIdentification(String identification,
        Pageable pageable);
    
    
    /**
     * Busca os transações de acordo com o nome passado como parametro
     * @param name
     * @return List<Transaction>
     */
    @Query("SELECT u FROM Transaction u"
         + " WHERE u.identification LIKE CONCAT('%', :identification, '%')"
         + " AND u.transactionType.id = :type"
         + " ORDER BY u.description")
    List<Transaction> searchByIdentificationAndType(@Param("identification") String identification, @Param("type") TransactionType type);
    
    /**
     * Busca os transações de acordo com o nome passado como parametro
     * @param name
     * @return List<Transaction>
     */
    @Query("SELECT u FROM Transaction u"
         + " WHERE (u.identification LIKE CONCAT('%', :identification, '%')"
         + "    OR  u.transactionParent.identification LIKE CONCAT('%', :identification, '%'))"
         + " ORDER BY u.description")
    Page<Transaction> searchByIdentification(@Param("identification") String identification, Pageable pageable);
    
    /**
     * Busca os transações de acordo com o nome passado como parametro
     * @param name
     * @return List<Transaction>
     */
    @Query("SELECT u FROM Transaction u"
         + " ORDER BY u.description")
    Page<Transaction> searchAll(Pageable pageable);
    
    /**
     * Busca os transações de acordo com o nome passado como parametro
     * @param name
     * @return List<Transaction>
     */
    @Query("SELECT u FROM Transaction u"
         + " WHERE (u.identification LIKE CONCAT('%', :identification, '%')"
         + "    OR  u.transactionParent.identification LIKE CONCAT('%', :identification, '%'))"
         + " ORDER BY u.description")
    List<Transaction> searchByIdentification(@Param("identification") String identification);
    
    /**
     * Busca os transações de acordo com o nome passado como parametro
     * @param name
     * @return List<Transaction>
     */
    @Query("SELECT u FROM Transaction u"
         + " WHERE u.identification LIKE CONCAT('%', :identification, '%')"
         + "   AND u.transactionParent IS NULL"
         + " ORDER BY u.identification")
    List<Transaction> searchTranactionsScenarioByIdentification(@Param("identification") String identification);

    /**
     * Retorna todas as transacoes que sao filhas da transação indicaad.
     * 
     * @param pageable
     * @return {@link Project}
     */
    List<Transaction> findByTransactionParentId(Long transactionParent);
}
