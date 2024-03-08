package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.LayoutOutputTransaction;

/**
 * Repositorio para a entidade LayoutInput.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface LayoutOutputTransactionRepository extends JpaCrudRepository<LayoutOutputTransaction, Long> {

    /**
     * Busca a lista de layouts de saída de acordo com a transação.
     * 
     * @param transactionId
     * @return Lista de layouts
     */
    List<LayoutOutputTransaction> findByTransactionId(Long transactionId);
    
}
