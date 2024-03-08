package br.com.scopus.simulador.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.scopus.simulador.repository.entity.TestMassInput;

/**
 * Repositorio para a entidade TestMassInput.
 * 
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 * @see http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Repository
public interface TestMassInputRepository extends JpaCrudRepository<TestMassInput, Long> {

    /**
     * Busca a lista condições de entrada para a massa de teste indicada.
     * 
     * @param testMassId
     * @return List<TestMassInput>
     */
    List<TestMassInput> findByTestMassId(Long testMassId);
    
}
