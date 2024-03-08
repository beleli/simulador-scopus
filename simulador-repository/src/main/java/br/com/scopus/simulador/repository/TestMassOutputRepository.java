package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.TestMassOutput;

/**
 * Repositorio para a entidade TestMassOutput.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface TestMassOutputRepository extends JpaCrudRepository<TestMassOutput, Long> {
    
    /**
     * Busca a lista de valores de saída para a massa indicada.
     * 
     * @param testMassId
     * @return List<TestMassOutput>
     */
    List<TestMassOutput> findByTestMassId(Long testMassId);
    
    TestMassOutput findById(Long id);

}
