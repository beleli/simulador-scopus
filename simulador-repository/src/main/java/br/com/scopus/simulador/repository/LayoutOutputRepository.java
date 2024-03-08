package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.LayoutOutput;

/**
 * Repositorio para a entidade LayoutInput.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface LayoutOutputRepository extends JpaCrudRepository<LayoutOutput, Long> {

    /***
     * Busca os campos de saida de acordo com o transacao.
     * 
     * @param transactionId
     * @return List<LayoutInput>
     */
    public List<LayoutOutput> findByLayoutOutputTransactionTransactionIdOrderByOrdinal(Long transactionId);
    
    /***
     * Busca os campos de saida de acordo com o layout de saída.
     * 
     * @param layoutOutputId
     * @return List<LayoutInput>
     */
    public List<LayoutOutput> findByLayoutOutputTransactionIdOrderByOrdinal(Long layoutOutputTransactionId);

}
