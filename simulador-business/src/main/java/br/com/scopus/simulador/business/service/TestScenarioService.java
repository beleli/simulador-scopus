package br.com.scopus.simulador.business.service;

import java.util.List;

import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.service.CrudService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.TestScenarioDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.entity.TestScenario;

/**
 * Interface de servico para transações.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public interface TestScenarioService  extends CrudService<TestScenarioDto, TestScenario>{

    /***
     * Sava a transação.
     * 
     * @param dto
     * @return dto
     * @throws ValidationException
     * @throws ServiceException
     */
    TestScenarioDto save(TestScenarioDto dto) throws ValidationException, ServiceException;

    /***
     * Busca a transação de acordo com a idenficação.
     * 
     * @param search
     * @return
     */
    PageDto<TestScenarioDto> findByTransactionId(PagedSearch<TestScenarioDto> search);

    /**
     * Lista os scenários de teste pela transação.
     * 
     * @param layoutOutputTransactionId
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> getComboBoxTestScenario(Long layoutOutputTransactionId);

    /**
     * Deleta todos os cenário de teste de acordo com o a transação.
     * @param id
     */
    void deleteTestScenarioByTransaction(Long id);
}
