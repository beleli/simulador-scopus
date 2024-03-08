package br.com.scopus.simulador.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.jerimum.fw.service.impl.AbstractCrudService;
import br.com.scopus.simulador.business.i18n.I18nKeys;
import br.com.scopus.simulador.business.service.TestScenarioService;
import br.com.scopus.simulador.business.service.TransactionService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.TestScenarioDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PageUtil;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.TestScenarioRepository;
import br.com.scopus.simulador.repository.entity.TestScenario;
import br.com.scopus.simulador.repository.entity.Transaction;

/**
 * Classe de servico para Senários de teste.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Service
public class TestScenarioServiceImpl extends AbstractCrudService<TestScenarioDto, TestScenario>
    implements TestScenarioService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TestScenarioRepository testScenarioRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    protected TestScenario buildEntityFromDto(TestScenarioDto dto) {
        return new TestScenario(dto.getId(), dto.getDescription(), new Transaction(dto.getTransactionId()));
    }

    @Override
    protected TestScenarioDto buildDtoFromEntity(TestScenario entity) {
        return new TestScenarioDto(entity.getId(), entity.getDescription(), entity.getTransaction().getId(),
            entity.getTransaction().getIdentification(), entity.getTransaction().getTransactionType().getName());
    }

    @Override
    protected JpaCrudRepository<TestScenario, Long> getRepository() {
        return this.testScenarioRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public TestScenarioDto save(TestScenarioDto dto) throws ValidationException, ServiceException {

        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        TestScenario testScneario = testScenarioRepository.save(buildEntityFromDto(dto));
        if (dto.getId() == null)
            dto.setId(testScneario.getId());

        return dto;
    }

    @Override
    public PageDto<TestScenarioDto> findByTransactionId(PagedSearch<TestScenarioDto> search) {
        TestScenarioDto dto = search.getItem();

        if (dto.getDescription() == null || dto.getDescription().trim().equals(""))
            dto.setDescription(null);

        Page<TestScenario> page = testScenarioRepository.findByTransacationIdAndDescription(dto.getTransactionId(),
            dto.getDescription(), PageUtil.createPageRequest(search));

        return new PageDto<>(buildDtoFromEntity(page.getContent()), page.getTotalElements());
    }

    @Override
    public List<ComboBoxDto> getComboBoxTestScenario(Long layoutOutputTransactionId) {
        List<ComboBoxDto> result = new ArrayList<ComboBoxDto>();
        Long transactionId = transactionService.getTransactionParentByOutput(layoutOutputTransactionId);
        List<TestScenario> tests = testScenarioRepository.findByTransactionIdOrderByDescription(transactionId);
        for (TestScenario t : tests) {
            result.add(new ComboBoxDto(t.getId(), t.getDescription()));
        }
        return result;
    }
    
    @Override
    @Transactional(readOnly = false)
    public void deleteTestScenarioByTransaction(Long id) {
        List<TestScenario> scenarios = testScenarioRepository.findByTransactionIdOrderByDescription(id);
        for (TestScenario scenario : scenarios ) {
            testScenarioRepository.delete(scenario);
        }
    }
}
