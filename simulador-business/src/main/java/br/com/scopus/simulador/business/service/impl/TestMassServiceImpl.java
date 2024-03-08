package br.com.scopus.simulador.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.jerimum.fw.service.impl.AbstractCrudService;
import br.com.scopus.simulador.business.i18n.I18nKeys;
import br.com.scopus.simulador.business.service.ProjectService;
import br.com.scopus.simulador.business.service.ProjectUserService;
import br.com.scopus.simulador.business.service.TestMassService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.TestMassDto;
import br.com.scopus.simulador.dto.TestMassItemDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PageUtil;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.LayoutInputRepository;
import br.com.scopus.simulador.repository.LayoutOutputRepository;
import br.com.scopus.simulador.repository.LayoutOutputTransactionRepository;
import br.com.scopus.simulador.repository.LayoutVersionRepository;
import br.com.scopus.simulador.repository.TestMassInputRepository;
import br.com.scopus.simulador.repository.TestMassOutputRepository;
import br.com.scopus.simulador.repository.TestMassRepository;
import br.com.scopus.simulador.repository.TransactionParentRepository;
import br.com.scopus.simulador.repository.TransactionRepository;
import br.com.scopus.simulador.repository.entity.LayoutInput;
import br.com.scopus.simulador.repository.entity.LayoutOutput;
import br.com.scopus.simulador.repository.entity.LayoutOutputTransaction;
import br.com.scopus.simulador.repository.entity.LayoutVersion;
import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.TestMass;
import br.com.scopus.simulador.repository.entity.TestMassInput;
import br.com.scopus.simulador.repository.entity.TestMassOutput;
import br.com.scopus.simulador.repository.entity.TestScenario;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.TransactionParent;
import br.com.scopus.simulador.repository.entity.enums.FieldType;
import scala.collection.mutable.StringBuilder;

@Service
public class TestMassServiceImpl extends AbstractCrudService<TestMassDto, TestMass> implements TestMassService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TestMassRepository testMassRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionParentRepository transactionParentRepository;

    @Autowired
    private LayoutOutputTransactionRepository layoutOutputTransactionRepository;

    @Autowired
    private LayoutOutputRepository layoutOutputRepository;

    @Autowired
    private LayoutInputRepository layoutInputRepository;

    @Autowired
    private TestMassOutputRepository testMassOutputRepository;

    @Autowired
    private LayoutVersionRepository layoutVersionRepository;

    @Autowired
    private TestMassInputRepository testMassInputRepository;

    @Autowired
    private ProjectUserService projectUserService;

    @Autowired
    private ProjectService projectService;

    @Override
    protected TestMassDto buildDtoFromEntity(TestMass entity) {
        return buildDtoFromEntity(entity, false);
    }

    protected TestMassDto buildDtoFromEntity(TestMass entity, boolean allFields) {
        if (entity == null)
            return null;

        TransactionParent parent = transactionParentRepository
            .findByPkLayoutOutputTransactionId(entity.getLayoutOutputTransaction().getId());

        TestMassDto dto = new TestMassDto(entity.getId(), entity.getTestScenario().getId(),
            parent.getLayoutOutputTransactionId(), parent.getIdentification(),
            entity.getProject() == null ? null : entity.getProject().getId(), entity.getDescription(),
            entity.getErrorAverage(), entity.getTimeout(), entity.getOccurrences(), entity.getRestart(),
            entity.getReturnCode(), null, null, null);

        if (!allFields)
            return dto;

        LayoutOutputTransaction layoutOutputTransaction = layoutOutputTransactionRepository
            .findOne(dto.getLayoutOutputTransactionId());

        dto.setInputList(getInputList(dto.getId(), layoutOutputTransaction.getTransaction().getId(),
            layoutOutputTransaction.getId()));

        dto.setOutputList(getOutputList(dto.getId(), dto.getLayoutOutputTransactionId()));

        return dto;
    }

    @Override
    protected TestMass buildEntityFromDto(TestMassDto dto) {
        if (dto == null)
            return null;

        return new TestMass(dto.getId(), new TestScenario(dto.getTestScenarioId()),
            new LayoutOutputTransaction(dto.getLayoutOutputTransactionId()),
            dto.getProjectId() == null ? null : new Project(dto.getProjectId()), dto.getDescription(),
            dto.getErrorAverage(), dto.getTimeout(), dto.getOccurrences(), dto.getRestart(), dto.getReturnCode());
    }

    @Override
    protected JpaCrudRepository<TestMass, Long> getRepository() {
        return testMassRepository;
    }

    @Override
    public List<TestMassItemDto> getInputList(Long testMassId, Long transactionId, Long layoutOutputTransactionId) {

        if (transactionId == null) {
            LayoutOutputTransaction layoutOutputTransaction = layoutOutputTransactionRepository
                .findOne(layoutOutputTransactionId);
            transactionId = layoutOutputTransaction.getTransaction().getId();
        }

        List<TestMassItemDto> dtos = new ArrayList<TestMassItemDto>();
        Transaction transaction = transactionRepository.findOne(transactionId);

        // adiciona os campos do transação pai
        if (transaction.getTransactionParent() != null) {
            dtos.addAll(
                getInputList(testMassId, transaction.getTransactionParent().getId(), layoutOutputTransactionId));
        }

        // Monta a lista de campos de entrada
        List<LayoutInput> entities = layoutInputRepository.findByTransactionIdOrderByOrdinal(transactionId);
        boolean first = true;
        Integer ordinal = dtos.size();

        for (LayoutInput input : entities) {
            String value = null;
            boolean readyOnly = false;

            // quando a transação tem pai o primeiro campo é sempre a versão
            if (transaction.getTransactionParent() != null) {
                if (first) {
                    value = transaction.getIdentification();
                }
            } else {
                // verficica se a mensagem tem multiplas saídas
                LayoutVersion outputVersion = layoutVersionRepository
                    .findByLayoutOutputTransactionIdAndLayoutInputId(layoutOutputTransactionId, input.getId());

                if (outputVersion != null)
                    value = outputVersion.getValue();
            }
            // primeiro campo com a transação é sempre bloqueado
            if (first) {
                readyOnly = true;
                first = false;
            } else {
                readyOnly = value != null;
            }

            dtos.add(new TestMassItemDto(null, input.getId(), input.getName(), input.getSize(),
                input.getFieldType().getName(), value, input.getOrdinal() + ordinal, readyOnly, null, null));
        }

        // Atribui os valores defeinidos para entrada.
        if (testMassId != null) {
            List<TestMassInput> inputList = testMassInputRepository.findByTestMassId(testMassId);
            for (TestMassInput input : inputList) {
                for (TestMassItemDto dto : dtos) {
                    if (input.getLayoutInput().getId().equals(dto.getLayoutId())) {
                        dto.setId(input.getId());
                        dto.setValue(input.getValue());
                    }
                }
            }
        }

        return dtos;
    }

    @Override
    public List<TestMassItemDto> getOutputList(Long testMassId, Long layoutOutputTransactionId) {
        List<TestMassItemDto> dtos = new ArrayList<TestMassItemDto>();

        // Monta a lista de campos de saída
        List<LayoutOutput> entities = layoutOutputRepository
            .findByLayoutOutputTransactionIdOrderByOrdinal(layoutOutputTransactionId);

        Long transactionId = 0L;
        if (entities.size() > 0)
            transactionId = entities.get(0).getLayoutOutputTransaction().getTransaction().getId();

        for (LayoutOutput output : entities) {

            List<LayoutInput> inputList = getLayoutInputActionList(transactionId, output.getFieldType(),
                output.getSize());
            List<ComboBoxDto> actions = new ArrayList<>();

            actions.add(new ComboBoxDto(-1L, "AV"));
            actions.add(new ComboBoxDto(-2L, "PB"));
            actions.add(new ComboBoxDto(-3L, "PZ"));
            for (LayoutInput input : inputList) {
                actions.add(new ComboBoxDto(input.getId(), "CE - " + input.getName()));
            }

            dtos.add(new TestMassItemDto(null, output.getId(), output.getName(), output.getSize(),
                output.getFieldType().getName(), null, output.getOrdinal(), false,
                output.getFieldType() == FieldType.ALPHANUMERIC ? -2L : -3L, actions));
        }

        // Atribui os valores defeinidos para saída.
        if (testMassId != null) {
            List<TestMassOutput> outputList = testMassOutputRepository.findByTestMassId(testMassId);
            for (TestMassOutput output : outputList) {
                for (TestMassItemDto dto : dtos) {
                    if (output.getLayoutOutput().getId().equals(dto.getLayoutId())) {
                        dto.setId(output.getId());
                        dto.setValue(null);
                        if (output.getLayoutInput() != null) {
                            dto.setCopyFieldId(output.getLayoutInput().getId());
                        } else {
                            if (isStringEmpty(output.getValue())) {
                                dto.setCopyFieldId(-2L);
                            } else if (output.getValue()
                                .equals(leftPad(null, output.getLayoutOutput().getSize(), '0'))) {
                                dto.setCopyFieldId(-3L);
                            } else {
                                dto.setCopyFieldId(-1L);
                                dto.setValue(output.getValue());
                            }
                        }
                    }
                }
            }
        }

        return dtos;
    }

    private List<LayoutInput> getLayoutInputActionList(Long transactionId, FieldType fieldType, Integer size) {
        Transaction transaction = transactionRepository.findOne(transactionId);
        List<LayoutInput> inputList = new ArrayList<>();
        if (transaction.getTransactionParent() != null)
            inputList.addAll(getLayoutInputActionList(transaction.getTransactionParent().getId(), fieldType, size));

        inputList.addAll(layoutInputRepository.findByTransactionIdAndFieldTypeAndSize(transactionId, fieldType, size));
        return inputList;
    }


    @Override
    @Transactional(readOnly = false)
    public TestMassDto save(TestMassDto dto) {
        if (dto == null || dto.getUserId() == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        if (!validadePermission(dto.getId(), dto.getUserId())) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.PermissaoInvalida.getKey());
            throw new ValidationException(ReturnCode.INVALID_PERMISSION.getCode(), msg);
        }

        if (!validadeInput(dto.getId(), dto.getLayoutOutputTransactionId(), dto.getInputList())) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.CamposDeInputInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_INPUT_LIST.getCode(), msg);
        }

        TestMass testMass = this.testMassRepository.save(buildEntityFromDto(dto));
        if (dto.getId() == null) {
            dto.setId(testMass.getId());
        }

        clearOutput(dto.getId(), dto.getLayoutOutputTransactionId());
        clearInput(dto.getId(), dto.getLayoutOutputTransactionId());

        // salva os campos de entrada que não são vazios
        for (TestMassItemDto item : dto.getInputList()) {
            if ((item.getValue() == null || isStringEmpty(item.getValue()))) {
                if (item.getId() != null)
                    testMassInputRepository.delete(item.getId());
            } else {
                if (!isStringEmpty(item.getValue().trim())) {
                    TestMassInput input = new TestMassInput(item.getId(), new TestMass(dto.getId()),
                        new LayoutInput(item.getLayoutId()), item.getValue());
                    testMassInputRepository.save(input);
                    if (item.getId() == null)
                        item.setId(input.getId());
                }
            }
        }

        // salva todos os campos de saída
        for (TestMassItemDto item : dto.getOutputList()) {
            LayoutOutput output = layoutOutputRepository.findOne(item.getLayoutId());
            String value = null;
            switch (item.getCopyFieldId().intValue()) {
            case -1:
                value = item.getValue();
                item.setCopyFieldId(null);
                break;
            case -2:
                value = leftPad(null, output.getSize(), ' ');
                item.setCopyFieldId(null);
                break;
            case -3:
                value = leftPad(null, output.getSize(), '0');
                item.setCopyFieldId(null);
                break;
            default:
                value = null;
                break;
            }

            TestMassOutput testMassOutput = new TestMassOutput(item.getId(), new TestMass(dto.getId()), output,
                item.getCopyFieldId() == null ? null : new LayoutInput(item.getCopyFieldId()), value);
            testMassOutputRepository.save(testMassOutput);

            if (item.getId() == null)
                item.setId(testMassOutput.getId());
        }

        return dto;
    }

    private boolean validadeInput(Long id, Long layoutOutputTransactionId, List<TestMassItemDto> inputList) {
        List<TestMass> testMassList = testMassRepository.findByLayoutOutputTransactionId(layoutOutputTransactionId);

        // Ainda não tem massa
        if (testMassList.size() == 0)
            return true;

        // Atualização da mesma massa
        if (testMassList.size() == 1 && testMassList.get(0).getId().equals(id))
            return true;

        // valida os campos de entrada
        List<TestMassInput> inputs = new ArrayList<TestMassInput>();
        for (TestMassItemDto item : inputList) {
            if (item.getValue() != null && item.getValue().trim() != "") {
                inputs.add(new TestMassInput(item.getId(), new TestMass(id), new LayoutInput(item.getLayoutId()),
                    item.getValue()));
            }
        }
        for (TestMass testMass : testMassList) {
            List<TestMassInput> otherInputs = testMassInputRepository.findByTestMassId(testMass.getId());
            if (testMass.getId().equals(id))
                continue;
            if (otherInputs.size() != inputs.size())
                continue;
            int equal = 0;
            for (TestMassInput otherInput : otherInputs) {
                for (TestMassInput input : inputs) {
                    if (otherInput.getLayoutInput().getId().equals(input.getLayoutInput().getId())) {
                        if (otherInput.getValue().equals(input.getValue()))
                            equal++;
                        break;
                    }
                }
            }
            if (equal == inputs.size())
                return false;
        }

        return true;
    }

    @Transactional(readOnly = false)
    private void clearInput(Long testMassId, Long layoutOutputTransactionId) {
        LayoutOutputTransaction layoutOutputTransaction = layoutOutputTransactionRepository
            .findOne(layoutOutputTransactionId);
        List<TestMassInput> inputList = testMassInputRepository.findByTestMassId(testMassId);
        for (TestMassInput input : inputList) {
            if (!validateTransactionInput(input.getLayoutInput().getTransaction().getId(),
                layoutOutputTransaction.getTransaction().getId()))
                testMassInputRepository.delete(input);
        }
    }

    private boolean validateTransactionInput(Long transactionId, Long transactionParentId) {
        if (transactionId.equals(transactionParentId))
            return true;

        Transaction t = transactionRepository.findOne(transactionParentId);
        if (t.getTransactionParent() != null)
            return validateTransactionInput(transactionId, t.getTransactionParent().getId());

        return false;
    }

    @Transactional(readOnly = false)
    private void clearOutput(Long testMassId, Long layoutOutputTransactionId) {
        List<TestMassOutput> outputList = testMassOutputRepository.findByTestMassId(testMassId);
        for (TestMassOutput output : outputList) {
            if (!output.getLayoutOutput().getLayoutOutputTransaction().getId().equals(layoutOutputTransactionId))
                testMassOutputRepository.delete(output);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(TestMassDto dto) {
        if (dto == null || dto.getId() == null || dto.getUserId() == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        TestMass entity = testMassRepository.findById(dto.getId());
        if (entity != null) {
            if (!validadePermission(entity.getId(), dto.getUserId())) {
                String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.PermissaoInvalida.getKey());
                throw new ValidationException(ReturnCode.INVALID_PERMISSION.getCode(), msg);
            }

            List<TestMassInput> inputs = testMassInputRepository.findByTestMassId(entity.getId());
            for (TestMassInput input : inputs) {
                testMassInputRepository.delete(input);
            }

            List<TestMassOutput> outputs = testMassOutputRepository.findByTestMassId(entity.getId());
            for (TestMassOutput output : outputs) {
                testMassOutputRepository.delete(output);
            }

            testMassRepository.delete(entity);
        }
    }

    private boolean validadePermission(Long testMassId, Long userId) {
        if (testMassId == null)
            return true;

        TestMass testMass = testMassRepository.findOne(testMassId);

        if (testMass.getProject() == null)
            return true;

        if (!projectService.isActiveProject(testMass.getProject().getId()))
            return true;

        if (projectUserService.isValidUserPermission(testMass.getProject().getId(), userId))
            return true;

        return false;
    }

    @Override
    public PageDto<TestMassDto> search(PagedSearch<TestMassDto> search) {
        if (search == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        TestMassDto dto = search.getItem();
        Long layoutOutputTransaction = dto == null ? null : dto.getLayoutOutputTransactionId();
        Long project = dto == null ? null : dto.getProjectId();

        Page<TestMass> page = this.testMassRepository.findByLayoutOutputTransactionIdAndProjectId(
            layoutOutputTransaction, project, PageUtil.createPageRequest(search));

        return new PageDto<>(buildDtoFromEntity(page.getContent()), page.getTotalElements());
    }

    @Override
    public TestMassDto findById(Long id) {
        if (id == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }
        return buildDtoFromEntity(testMassRepository.findOne(id), true);
    }

    private boolean isStringEmpty(String string) {
        return string == null || string.trim().equals("");
    }

    private String leftPad(String string, Integer size, char c) {
        if (string == null)
            string = new String();

        StringBuilder sb = new StringBuilder(string);
        while (sb.size() < size) {
            sb.append(c);
        }
        return sb.toString();
    }
    
    @Override
    @Transactional(readOnly = false)
    public void deleteTestMassByLayoutOutputTransaction(Long id, Long userId) {
        List<TestMass> testMasses = testMassRepository.findByLayoutOutputTransactionId(id);
        for (TestMass testMass: testMasses) {
            TestMassDto dto = buildDtoFromEntity(testMass, false);
            dto.setUserId(userId);
            delete(dto);
        }
    }
}
