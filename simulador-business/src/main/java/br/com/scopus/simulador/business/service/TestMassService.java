package br.com.scopus.simulador.business.service;

import java.util.List;

import br.com.jerimum.fw.service.CrudService;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.TestMassDto;
import br.com.scopus.simulador.dto.TestMassItemDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.entity.TestMass;

/**
 * Interface de servico para transações.
 * 
 * @author Yuslley Silva Fagundes - yfagundes@scopus.com.br
 * @since 1.0
 */
public interface TestMassService extends CrudService<TestMassDto, TestMass> {

    /**
     * Salva a entidade passada como parametro.
     * 
     * @param dto
     * @param userId
     * @return dto
     */
    TestMassDto save(TestMassDto dto);

    /**
     * Busca a Massa de testes de acordo com o parametro.
     * 
     * @param search
     * @return PageDto<TestMassDto>
     */
    PageDto<TestMassDto> search(PagedSearch<TestMassDto> search);

    /**
     * Busca o dto pelo Id.
     * 
     * @param testMassId
     * @return TestMassDto
     */
    TestMassDto findById(Long testMassId);

    /**
     * Deleta a entidade.
     * 
     * @param testMassId
     * @param userId
     */
    void delete(TestMassDto dto);

    /**
     * Busca a lista de campos de entrada.
     * @param testMassId
     * @param transactionId
     * @param transactionOutputTransactionId
     * @return List<TestMassItemDto>
     */
    List<TestMassItemDto> getInputList(Long testMassId, Long transactionId, Long transactionOutputTransactionId);

    /**
     * Busca a lsita de campos de saída.
     * @param testMassId
     * @param layoutOutputTransactionId
     * @return
     */
    List<TestMassItemDto> getOutputList(Long testMassId, Long layoutOutputTransactionId);

    /**
     * Delete todas as massas de teste de acordo com o layout de saída indicado
     * @param id
     * @param userId
     */
    void deleteTestMassByLayoutOutputTransaction(Long id, Long userId);

}
