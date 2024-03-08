package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.TestMass;

/**
 * Repositorio para a entidade TestMass.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Yuslley Silva Fagundes - yfagundes@scopus.com.br
 * @since 1.0
 */
@Repository
public interface TestMassRepository extends JpaCrudRepository<TestMass, Long> {

    /**
     * Busca todas as massas de acordo com o layout de saída.
     * 
     * @param layoutOutputTransactionId
     * @return List<TestMass>
     */
    @Query("SELECT t FROM TestMass t"
         + " WHERE (:layoutOutputTransactionId IS NULL OR t.layoutOutputTransaction.id = :layoutOutputTransactionId)"
         + "   AND (:projectId IS NULL OR t.project.id = :projectId)"
         + " ORDER BY t.description")
    Page<TestMass> findByLayoutOutputTransactionIdAndProjectId(
        @Param("layoutOutputTransactionId") Long layoutOutputTransactionId, @Param("projectId") Long projectId,
        Pageable pageable);
    
    /**
     * Busca todas as massas de teste pelo layout de saída
     * 
     * @param layoutOutputTransactionId
     * @return List<TestMass>
     */
    List<TestMass> findByLayoutOutputTransactionId(Long layoutOutputTransactionId);
    
    /**
     * Busca a entidade pelo Id.
     * @param id
     * @return TestMass
     */
    TestMass findById(Long id);
}
