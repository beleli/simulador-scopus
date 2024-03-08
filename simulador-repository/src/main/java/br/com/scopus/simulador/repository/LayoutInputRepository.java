package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.LayoutInput;
import br.com.scopus.simulador.repository.entity.enums.FieldType;

/**
 * Repositorio para a entidade LayoutInput.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface LayoutInputRepository extends JpaCrudRepository<LayoutInput, Long> {
    
    /***
     * Busca os campos de entrada de acorda com o transação.
     * 
     * @param transactionId
     * @return List<LayoutInput>
     */
    public List<LayoutInput> findByTransactionIdOrderByOrdinal(Long transactionId);
    
    /***
     * Busca os campos de entrada de acorda com o transação, tipo de entrada e tamanho.
     * 
     * @param transactionId
     * @return List<LayoutInput>
     */
    public List<LayoutInput> findByTransactionIdAndFieldTypeAndSize(Long transactionId, FieldType fieldType, Integer Size);

}
