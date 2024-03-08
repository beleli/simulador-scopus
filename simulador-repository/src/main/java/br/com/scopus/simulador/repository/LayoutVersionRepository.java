package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.LayoutVersion;

/**
 * Repositorio para a entidade LayoutInput.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface LayoutVersionRepository extends JpaCrudRepository<LayoutVersion, Long> {

    /**
     * Busca a versão
     * 
     * @param layoutOutputTransactionId
     * @param layoutInputId
     * @return Versão
     */
    LayoutVersion findByLayoutOutputTransactionIdAndLayoutInputId(Long layoutOutputTransactionId, Long layoutInputId);
    
    /**
     * Busca a lista de versões pela transação
     * 
     * @param layoutOutputTransactionId
     * @param layoutInputId
     * @return Lista de versões
     */
    List<LayoutVersion> findByLayoutOutputTransactionId(Long layoutOutputTransactionId);
   
    /**
     * Busca a lista de versões pela transação
     * 
     * @param layoutOutputTransactionId
     * @param layoutInputId
     * @return Lista de versões
     */
    List<LayoutVersion> findByLayoutInputTransactionId(Long layoutInputTransactionId);
    
    /**
     * Conta/valida se o layoutOutputTransaction possui multipas saídas.
     * 
     * @param transactionOutputTransactionId
     * @return Total de campos de versionamento
     */
    Long countByLayoutOutputTransactionId(Long layoutOutputTransactionId);

    /**
     * Busca a lista de versões pela transação
     * 
     * @param layoutOutputTransactionId
     * @param layoutInputId
     * @return Lista de versões
     */
    List<LayoutVersion> findByLayoutOutputTransactionTransactionId(Long transactionId);
}

