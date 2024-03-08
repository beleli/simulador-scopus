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
import br.com.scopus.simulador.business.service.TestMassService;
import br.com.scopus.simulador.business.service.TestScenarioService;
import br.com.scopus.simulador.business.service.TransactionService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.TransactionDto;
import br.com.scopus.simulador.dto.TransactionItemDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PageUtil;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.LayoutInputRepository;
import br.com.scopus.simulador.repository.LayoutOutputRepository;
import br.com.scopus.simulador.repository.LayoutOutputTransactionRepository;
import br.com.scopus.simulador.repository.LayoutVersionRepository;
import br.com.scopus.simulador.repository.TransactionParentRepository;
import br.com.scopus.simulador.repository.TransactionRepository;
import br.com.scopus.simulador.repository.entity.LayoutInput;
import br.com.scopus.simulador.repository.entity.LayoutOutput;
import br.com.scopus.simulador.repository.entity.LayoutOutputTransaction;
import br.com.scopus.simulador.repository.entity.LayoutVersion;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.TransactionParent;
import br.com.scopus.simulador.repository.entity.converter.FieldTypeConverter;
import br.com.scopus.simulador.repository.entity.converter.TransactionEncodingConverter;
import br.com.scopus.simulador.repository.entity.converter.TransactionTypeConverter;
import br.com.scopus.simulador.repository.entity.enums.FieldType;
import br.com.scopus.simulador.repository.entity.enums.TransactionEncoding;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;

/**
 * Classe de servico para transações.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Service
public class TransactionServiceImpl extends AbstractCrudService<TransactionDto, Transaction>
    implements TransactionService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionParentRepository transactionParentRepository;

    @Autowired
    private LayoutInputRepository layoutInputRepository;

    @Autowired
    private LayoutOutputRepository layoutOutputRepository;

    @Autowired
    private LayoutVersionRepository layoutVersionRepository;

    @Autowired
    private LayoutOutputTransactionRepository layoutOutputTransactionRepository;

    @Autowired
    private FieldTypeConverter fieldTypeConverter;

    @Autowired
    private TransactionTypeConverter transactionTypeConverter;

    @Autowired
    private TransactionEncodingConverter transactionEncodingConverter;
    
    @Autowired
    private TestMassService testMassService;
    
    @Autowired
    private TestScenarioService testScenarioService;

    Integer qtdBytesOutput = 0;
    Integer qtdBytesInput = 0;

    @Override
    protected JpaCrudRepository<Transaction, Long> getRepository() {
        return this.transactionRepository;
    }

    @Override
    protected TransactionDto buildDtoFromEntity(Transaction entity) {
        TransactionDto dto = null;
        if (entity != null) {

            Integer transactionTypeId = entity.getTransactionType().getId();
            String transactionTypeName = entity.getTransactionType().getName();
            Integer transactionEncoding = entity.getTransactionEncoding().getId();
            String transactionEncodingName = entity.getTransactionEncoding().getName();
            Long transactionParent = entity.getTransactionParent() == null ? null
                : entity.getTransactionParent().getId();
            Integer transactionRouter = entity.getRouterType() == null ? null : entity.getRouterType().getId();

            dto = new TransactionDto(entity.getId(), transactionTypeId, transactionTypeName, transactionEncoding,
                transactionEncodingName, entity.getIdentification(), entity.getDescription(), transactionParent,
                transactionRouter, entity.isParent(), entity.getLastAccess(), null, null, false, null, null,
                qtdBytesInput, qtdBytesOutput, null);
        }
        return dto;
    }

    @Override
    public TransactionDto getDtoByIdAndIdOutPut(TransactionDto dto) {
        if (dto == null || dto.getId() == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        return buildDtoFromEntity(transactionRepository.findOne(dto.getId()), dto.getTransactionOutputId(), true);
    }

    private TransactionDto buildDtoFromEntity(Transaction entity, Long layoutOutputTransactionId, boolean allFields) {
        TransactionDto dto = buildDtoFromEntity(entity);
        dto.setTransactionOutputId(layoutOutputTransactionId);

        if (!allFields) {
            return dto;
        }

        if (layoutOutputTransactionId != null && layoutOutputTransactionId > 0) {
            LayoutOutputTransaction outputTransaction = layoutOutputTransactionRepository
                .findOne(layoutOutputTransactionId);
            dto.setTransactionOutputDescription(outputTransaction.getDescription());
        }
        dto.setMultipleOutputs(layoutVersionRepository.countByLayoutOutputTransactionId(layoutOutputTransactionId) > 0);

        // Campos de entrada
        qtdBytesInput = 0;
        List<TransactionItemDto> layoutInput = getLayoutInput(entity.getId(), layoutOutputTransactionId, false);
        dto.setInputFields(layoutInput);
        dto.setQtdBytesInput(qtdBytesInput);

        // Campos de saída
        List<TransactionItemDto> layoutOutput = null;
        qtdBytesOutput = 0;
        if (layoutOutputTransactionId != null && layoutOutputTransactionId > 0) {
            layoutOutput = getLayoutOutput(layoutOutputTransactionId);
            dto.setOutputFields(layoutOutput);
            dto.setQtdBytesOutput(qtdBytesOutput);
        }

        return dto;
    }

    @Override
    protected Transaction buildEntityFromDto(TransactionDto dto) {
        Transaction entity = null;
        if (dto != null) {

            TransactionType type = transactionTypeConverter.convertToEntityAttribute(dto.getTransactionTypeId());
            TransactionEncoding encoding = transactionEncodingConverter
                .convertToEntityAttribute(dto.getTransactionEncodingId());
            Transaction parent = dto.getTransactionParentId() != null ? new Transaction(dto.getTransactionParentId())
                : null;
            TransactionType routerType = transactionTypeConverter.convertToEntityAttribute(dto.getRouterTypeId());

            entity = new Transaction(dto.getId(), type, encoding, dto.getName().toUpperCase(), dto.getDescription(),
                parent, routerType, dto.isParent(), dto.getLastAccess());
        }
        return entity;
    }

    @Override
    public List<TransactionItemDto> getLayoutInput(Long transactionId, Long transactionOutputTransactionId,
        boolean parent) {
        List<TransactionItemDto> dtos = new ArrayList<TransactionItemDto>();
        Transaction transaction = transactionRepository.findOne(transactionId);
        // adiciona os campos do transação pai
        if (transaction.getTransactionParent() != null) {
            dtos.addAll(
                getLayoutInput(transaction.getTransactionParent().getId(), transactionOutputTransactionId, true));
        }
        List<LayoutInput> entities = this.layoutInputRepository.findByTransactionIdOrderByOrdinal(transactionId);
        boolean first = true;
        Integer ordinal = dtos.size();

        for (LayoutInput input : entities) {
            qtdBytesInput += input.getSize();

            boolean version = false;
            String value = null;

            // quando a transação tem pai o primeiro campo é sempre a versão
            if (transaction.getTransactionParent() != null) {
                if (first) {
                    version = true;
                    first = false;
                    value = transaction.getIdentification();
                }
            } else {
                // verficica se a mensagem tem multiplas saídas
                LayoutVersion outputVersion = layoutVersionRepository
                    .findByLayoutOutputTransactionIdAndLayoutInputId(transactionOutputTransactionId, input.getId());

                if (outputVersion != null) {
                    version = true;
                    value = outputVersion.getValue();
                }
            }

            dtos.add(new TransactionItemDto(input.getId(), input.getFieldType().getId(), input.getFieldType().getName(),
                input.getName(), input.getSize(), input.getOrdinal() + ordinal, version, value, parent));
        }

        return dtos;
    }

    @Override
    public List<TransactionItemDto> getLayoutOutput(Long layoutOutputTransactionId) {
        List<TransactionItemDto> dtos = new ArrayList<TransactionItemDto>();
        List<LayoutOutput> entities = this.layoutOutputRepository
            .findByLayoutOutputTransactionIdOrderByOrdinal(layoutOutputTransactionId);
        for (LayoutOutput output : entities) {
            qtdBytesOutput += output.getSize();
            dtos.add(
                new TransactionItemDto(output.getId(), output.getFieldType().getId(), output.getFieldType().getName(),
                    output.getName(), output.getSize(), output.getOrdinal(), false, null, false));
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> findByIdentification(String name) {
        List<TransactionParent> lista = transactionParentRepository.searchByIdentification(name);
        List<TransactionDto> listaDto = buildDtoFromView(lista, true);
        return listaDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> findByIdentificationAndType(String name, Integer type) {
        TransactionTypeConverter converter = new TransactionTypeConverter();
        List<Transaction> lista = this.transactionRepository.searchByIdentificationAndType(name,
            converter.convertToEntityAttribute(type));
        List<TransactionDto> listaDto = buildDtoFromEntity(lista);
        return listaDto;
    }

    @Override
    @Transactional(readOnly = false)
    public TransactionDto save(TransactionDto dto) throws ValidationException, ServiceException {
        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        if (dto.getInputFields() == null || dto.getInputFields().size() == 0) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.CamposDeInputVazio.getKey());
            throw new ValidationException(ReturnCode.INVALID_INPUT_FIELDS.getCode(), msg);
        } else {
            boolean find = false;
            for (TransactionItemDto input : dto.getInputFields()) {
                if (!input.isParent()) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.CamposDeInputVazio.getKey());
                throw new ValidationException(ReturnCode.INVALID_INPUT_FIELDS.getCode(), msg);
            }
        }

        if (!dto.isParent() && (dto.getInputFields() == null || dto.getOutputFields().size() == 0)) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.CamposDeOutputVazio.getKey());
            throw new ValidationException(ReturnCode.INVALID_OUTPUT_FIELDS.getCode(), msg);
        }

        if (!validateVersion(dto.getId(), dto.getTransactionOutputId(), dto.getInputFields())) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.VersaoInvalidaTransacao.getKey());
            throw new ValidationException(ReturnCode.INVALID_TRANSACTION_VERSION.getCode(), msg);
        }

        // ajusta a herança da transação.
        if (dto.getTransactionParentId() != null) {
            Transaction parent = transactionRepository.findOne(dto.getTransactionParentId());
            dto.setTransactionTypeId(parent.getTransactionType().getId());
            dto.setTransactionEncodingId(parent.getTransactionEncoding().getId());
            dto.setRouterTypeId(null);
        }

        Transaction trans = transactionRepository.save(buildEntityFromDto(dto));
        if (dto.getId() == null)
            dto.setId(trans.getId());

        LayoutOutputTransaction layoutOutoutTransaction = saveLayoutOutputTransaction(dto.getTransactionOutputId(),
            dto.getId(), dto.getTransactionOutputDescription());

        // salva o layout de input
        saveLayoutInput(dto.getInputFields(), trans, layoutOutoutTransaction.getId(),
            dto.getTransactionParentId() == null);

        if (dto.isParent()) {
            // transações pai não possuem layouts de saída ou versões
            deleteLayoutVersion(dto.getId());
            deleteLayoutOutput(dto.getOutputFields(), dto.getId());
            deleteLayoutOutputTransaction(dto.getId());
        } else {
            saveLayoutOutput(dto.getOutputFields(), layoutOutoutTransaction.getId());
        }

        return dto;
    }

    private boolean validateVersion(Long transactionId, Long layoutOutputTransactionId,
        List<TransactionItemDto> inputList) {
        List<LayoutOutputTransaction> outputs = layoutOutputTransactionRepository.findByTransactionId(transactionId);

        // Ainda não tem versão
        if (outputs.size() == 0)
            return true;

        // Atualização da mesma versão
        if (outputs.size() == 1 && outputs.get(0).getId() == layoutOutputTransactionId)
            return true;

        // valida os campos versionados
        List<LayoutVersion> versions = new ArrayList<LayoutVersion>();
        for (TransactionItemDto item : inputList) {
            if (item.getValue() != null && item.getValue().trim() != "") {
                versions.add(new LayoutVersion(null, new LayoutInput(item.getId()),
                    new LayoutOutputTransaction(layoutOutputTransactionId), item.getValue()));
            }
        }
        for (LayoutOutputTransaction output : outputs) {
            if (output.getId() != layoutOutputTransactionId) {
                List<LayoutVersion> otherVersions = layoutVersionRepository
                    .findByLayoutOutputTransactionId(output.getId());
                if (otherVersions.size() != versions.size())
                    return false;
                for (LayoutVersion otherVersion : otherVersions) {
                    boolean find = false;
                    for (LayoutVersion version : versions) {
                        if (otherVersion.getLayoutInput().getId() == version.getLayoutInput().getId())
                            find = true;
                        if (!find) {
                            return false;
                        } else {
                            if (otherVersion.getValue().equals(version.getValue()))
                                return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private LayoutOutputTransaction saveLayoutOutputTransaction(Long layoutOutputTransactionId, Long transactionId,
        String description) {
        LayoutOutputTransaction layoutOutputTransaction = new LayoutOutputTransaction(layoutOutputTransactionId,
            new Transaction(transactionId), description);
        return layoutOutputTransactionRepository.save(layoutOutputTransaction);
    }

    private void deleteLayoutVersion(Long transactionId) {
        List<LayoutVersion> versions = layoutVersionRepository.findByLayoutInputTransactionId(transactionId);
        for (LayoutVersion version : versions) {
            layoutVersionRepository.delete(version);
        }
    }

    private void deleteLayoutOutput(List<TransactionItemDto> dtos, Long transactionId) {
        List<LayoutOutput> layout = layoutOutputRepository
            .findByLayoutOutputTransactionTransactionIdOrderByOrdinal(transactionId);
        layoutOutputRepository.delete(layout);
        layout = layoutOutputRepository.findByLayoutOutputTransactionTransactionIdOrderByOrdinal(transactionId);
    }

    private void deleteLayoutOutputTransaction(Long transactionId) {
        List<LayoutOutputTransaction> layouts = layoutOutputTransactionRepository.findByTransactionId(transactionId);
        for (LayoutOutputTransaction layout : layouts)
            layoutOutputTransactionRepository.delete(layout);
    }

    private void saveLayoutOutput(List<TransactionItemDto> dtos, Long layoutOutoutTransactionId) {
        List<LayoutOutput> outputs = layoutOutputRepository
            .findByLayoutOutputTransactionIdOrderByOrdinal(layoutOutoutTransactionId);

        // remove os campos da lista inicial
        for (LayoutOutput output : outputs) {
            boolean find = false;
            for (TransactionItemDto dto : dtos) {
                if (dto.getId() == output.getId()) {
                    find = true;
                    break;
                }
            }
            if (!find)
                layoutOutputRepository.delete(output.getId());
        }

        // insere/atualiza os campos da lista
        for (TransactionItemDto dto : dtos) {
            layoutOutputRepository
                .save(new LayoutOutput(dto.getId(), new LayoutOutputTransaction(layoutOutoutTransactionId),
                    fieldTypeConverter.convertToEntityAttribute(dto.getFieldTypeId()), dto.getName().trim(),
                    dto.getSize(), dto.getOrdinal()));
        }
    }

    private void saveLayoutInput(List<TransactionItemDto> dtos, Transaction transaction, Long layoutOutputTransactionId,
        boolean isVersion) {
        List<LayoutInput> inputs = layoutInputRepository.findByTransactionIdOrderByOrdinal(transaction.getId());

        // remove os campos da lista inicial
        for (LayoutInput input : inputs) {
            boolean find = false;
            for (TransactionItemDto dto : dtos) {
                if (input.getId().equals(dto.getId())) {
                    find = true;
                    break;
                }
            }
            if (!find && input.getTransaction().getId().equals(transaction.getId()))
                layoutInputRepository.delete(input.getId());
        }

        // insere/atualiza os campos da lista
        Integer ordinal = 0;
        boolean versionated = false;
        for (TransactionItemDto dto : dtos) {
            if (!dto.isParent()) {
                LayoutInput layoutInput = layoutInputRepository.save(new LayoutInput(dto.getId(), transaction,
                    fieldTypeConverter.convertToEntityAttribute(dto.getFieldTypeId()), dto.getName().trim(),
                    dto.getSize(), dto.getOrdinal() - ordinal));

                // Gera versão do layout de saída de acordo com a versão de entrada
                if (isVersion && dto.getValue() != null && dto.getValue().trim() != "") {
                    versionated = true;
                    LayoutVersion version = layoutVersionRepository.findByLayoutOutputTransactionIdAndLayoutInputId(
                        layoutOutputTransactionId, layoutInput.getId());
                    if (version == null)
                        version = new LayoutVersion();
                    version.setLayoutInput(new LayoutInput(layoutInput.getId()));
                    version.setLayoutOutputTransaction(new LayoutOutputTransaction(layoutOutputTransactionId));
                    version.setValue(dto.getValue());
                    layoutVersionRepository.save(version);
                }
            } else {
                ordinal++;
            }
        }
        if (!versionated)
            deleteLayoutVersion(transaction.getId());
    }

    @Override
    public PageDto<TransactionDto> findByTransaction(PagedSearch<TransactionDto> pagedSearch) {
        TransactionDto dto = pagedSearch.getItem();

        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        if (dto.getName() == null || dto.getName().trim().equals(""))
            dto.setName(null);

        Page<TransactionParent> page = transactionParentRepository
            .findByIdentificationOrderByIdentification(dto.getName(), PageUtil.createPageRequest(pagedSearch));

        return new PageDto<>(buildDtoFromView(page.getContent(), false), page.getTotalElements());
    }

    private List<TransactionDto> buildDtoFromView(List<TransactionParent> content, boolean allFields) {
        List<TransactionDto> entityList = new ArrayList<TransactionDto>();
        for (TransactionParent t : content) {
            TransactionDto dto = buildDtoFromView(t, allFields);
            entityList.add(dto);
        }
        return entityList;
    }

    private TransactionDto buildDtoFromView(TransactionParent view, boolean allFields) {
        Transaction transaction = new Transaction(view.getId(), view.getTransactionType(),
            view.getTransactionEncoding(), view.getIdentification(), view.getDescription(), view.getTransactionParent(),
            view.getTransactionType(), view.isParent(), view.getLastAccess());
        return buildDtoFromEntity(transaction, view.getLayoutOutputTransactionId(), allFields);
    }

    @Override
    public List<ComboBoxDto> getComboBoxTransactionType() {
        return getListTransactionType();
    }

    private List<ComboBoxDto> getListTransactionType() {
        List<ComboBoxDto> result = new ArrayList<ComboBoxDto>();
        for (TransactionType t : TransactionType.values()) {
            result.add(new ComboBoxDto(t.getId().longValue(), t.getName()));
        }
        return result;
    }


    @Override
    public List<ComboBoxDto> getComboBoxTransactionFormats() {
        List<ComboBoxDto> result = new ArrayList<ComboBoxDto>();
        for (TransactionEncoding e : TransactionEncoding.values()) {
            result.add(new ComboBoxDto(e.getId().longValue(), e.getName()));
        }
        return result;
    }

    @Override
    public List<ComboBoxDto> getComboBoxLayoutFieldType() {
        List<ComboBoxDto> result = new ArrayList<ComboBoxDto>();
        for (FieldType t : FieldType.values()) {
            result.add(new ComboBoxDto(t.getId().longValue(), t.getName()));
        }
        return result;
    }

    @Override
    public List<ComboBoxDto> getComboBoxTransactionParent() {
        List<ComboBoxDto> result = new ArrayList<ComboBoxDto>();
        List<TransactionParent> transactions = transactionParentRepository.findByParentTrueOrderByIdentification();
        for (TransactionParent t : transactions) {
            result.add(new ComboBoxDto(t.getId(), t.getIdentification()));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComboBoxDto> getComboBoxScenarioTranactions(String name) {
        List<ComboBoxDto> result = new ArrayList<ComboBoxDto>();
        List<Transaction> transactions = transactionRepository.searchTranactionsScenarioByIdentification(name);
        for (Transaction t : transactions) {
            result.add(new ComboBoxDto(t.getId(), t.getIdentification()));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComboBoxDto> getComboBoxTestMassTranactions(String name) {
        List<ComboBoxDto> result = new ArrayList<ComboBoxDto>();
        List<TransactionParent> transactions = transactionParentRepository
            .searchTranactionsScenarioByIdentification(name);
        for (TransactionParent t : transactions) {
            result.add(new ComboBoxDto(t.getLayoutOutputTransactionId(), t.getIdentification()));
        }
        return result;
    }

    @Override
    public Long getTransactionParentByOutput(Long layoutOutputTransactionId) {
        TransactionParent transaction = transactionParentRepository
            .findByPkLayoutOutputTransactionId(layoutOutputTransactionId);
        if (transaction.getTransactionParent() != null) {
            return getTransactionParent(transaction.getId());
        }
        return transaction.getId();
    }

    private Long getTransactionParent(Long id) {
        Transaction transaction = transactionRepository.findOne(id);
        if (transaction.getTransactionParent() != null) {
            return getTransactionParent(transaction.getTransactionParent().getId());
        } else {
            return transaction.getId();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public TransactionDto deleteDto(TransactionDto dto) throws ValidationException, ServiceException {
        if (dto == null || dto.getId() == null || dto.getUserId() == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }
        return deleteDtoById(dto.getId(), dto.getUserId());
    }
    
    @Transactional(readOnly = false)
    private TransactionDto deleteDtoById(Long id, Long userId) throws ValidationException, ServiceException {
        TransactionDto result = getDtoById(id);

        if (id == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        List<Transaction> transactions = transactionRepository.findByTransactionParentId(id);
        for (Transaction t : transactions) {
            deleteDtoById(t.getId(), userId);
        }
        deleteTransaction(id, userId);

        return result;
    }

    @Transactional(readOnly = false)
    private void deleteTransaction(Long id, Long userId) {
        List<LayoutOutputTransaction> outputTransactions = layoutOutputTransactionRepository.findByTransactionId(id);
        for (LayoutOutputTransaction outputTransaction: outputTransactions) {
            testMassService.deleteTestMassByLayoutOutputTransaction(outputTransaction.getId(), userId);
            List<LayoutOutput> outputs = layoutOutputRepository.findByLayoutOutputTransactionTransactionIdOrderByOrdinal(id);
            for (LayoutOutput output : outputs) {
                layoutOutputRepository.delete(output);
            }
            layoutOutputTransactionRepository.delete(outputTransaction);
        }
        
        List<LayoutInput> inputs = layoutInputRepository.findByTransactionIdOrderByOrdinal(id);
        for (LayoutInput input : inputs) {
            List<LayoutVersion> versions = layoutVersionRepository.findByLayoutInputTransactionId(id);
            for (LayoutVersion version : versions) {
                layoutVersionRepository.delete(version);
            }
            layoutInputRepository.delete(input);
        }
        
        testScenarioService.deleteTestScenarioByTransaction(id);
        transactionRepository.delete(id);
    }
}
