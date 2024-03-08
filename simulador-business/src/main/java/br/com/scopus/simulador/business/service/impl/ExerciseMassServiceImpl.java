package br.com.scopus.simulador.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.jerimum.fw.service.impl.AbstractCrudService;
import br.com.scopus.simulador.business.i18n.I18nKeys;
import br.com.scopus.simulador.business.service.ExerciseMassService;
import br.com.scopus.simulador.business.simulator.IsdSimulator;
import br.com.scopus.simulador.business.simulator.ServTranSimulator;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.ExerciseMassDto;
import br.com.scopus.simulador.dto.ExerciseMassItemDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
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
import br.com.scopus.simulador.repository.entity.TestMass;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.TransactionParent;
import br.com.scopus.simulador.repository.entity.enums.Mechanism;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;

@Service
public class ExerciseMassServiceImpl extends AbstractCrudService<ExerciseMassDto, TestMass>
    implements ExerciseMassService {

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LayoutVersionRepository layoutVersionRepository;

    @Autowired
    private LayoutOutputTransactionRepository layoutOutputTransactionRepository;
    
    @Autowired
    private LayoutInputRepository layoutInputRepository;
    
    @Autowired
    private LayoutOutputRepository layoutOutputRepository;
    
    @Autowired
    private TransactionParentRepository transactionParentRepository;
    
    @Autowired
    private IsdSimulator isdSimulator;
    
    @Autowired
    private ServTranSimulator servTranSimulator;

    @Override
    protected ExerciseMassDto buildDtoFromEntity(TestMass arg0) {
        return null;
    }

    @Override
    protected TestMass buildEntityFromDto(ExerciseMassDto arg0) {
        return null;
    }

    @Override
    protected JpaCrudRepository<TestMass, Long> getRepository() {
        return null;
    }

    @Override
    public List<ExerciseMassItemDto> getInputList(Long transactionOutputTransactionId) {
        return getInputList(null, transactionOutputTransactionId);
    }

    private List<ExerciseMassItemDto> getInputList(Long transactionId, Long transactionOutputTransactionId) {
        if (transactionId == null) {
            LayoutOutputTransaction layoutOutputTransaction = layoutOutputTransactionRepository
                .findOne(transactionOutputTransactionId);
            transactionId = layoutOutputTransaction.getTransaction().getId();
        }

        List<ExerciseMassItemDto> dtos = new ArrayList<>();
        Transaction transaction = transactionRepository.findOne(transactionId);

        // adiciona os campos do transação pai
        if (transaction.getTransactionParent() != null) {
            dtos.addAll(getInputList(transaction.getTransactionParent().getId(), transactionOutputTransactionId));
        }

        // Monta a lista de campos de entrada
        List<LayoutInput> entities = layoutInputRepository.findByTransactionIdOrderByOrdinal(transactionId);
        boolean first = true;
        Integer ordinal = dtos.size();

        for (LayoutInput input : entities) {
            String value = null;

            // quando a transação tem pai o primeiro campo é sempre a versão
            if (transaction.getTransactionParent() != null) {
                if (first) {
                    value = transaction.getIdentification();
                }
            } else {
                // verficica se a mensagem tem multiplas saídas
                LayoutVersion outputVersion = layoutVersionRepository
                    .findByLayoutOutputTransactionIdAndLayoutInputId(transactionOutputTransactionId, input.getId());

                if (outputVersion != null)
                    value = outputVersion.getValue();
            }
            // primeiro campo com a transação é sempre bloqueado
            if (first) {
                first = false;
                value = transaction.getIdentification();
            }

            dtos.add(new ExerciseMassItemDto(input.getId(), input.getName(), input.getSize(),
                input.getFieldType().getName(), value, input.getOrdinal() + ordinal, value != null));
        }

        return dtos;
    }

    @Override
    public List<ComboBoxDto> getComboBoxMechanisms() {
        List<ComboBoxDto> dtos = new ArrayList<>();
        for (Mechanism p : Mechanism.values()) {
            dtos.add(new ComboBoxDto(p.getId().longValue(), p.getName()));
        }
        return dtos;
    }

    @Override
    public List<ComboBoxDto> getComboBoxRouters(Long layoutOutputTransactionId) {
        LayoutOutputTransaction layoutOutputTransaction = layoutOutputTransactionRepository.findOne(layoutOutputTransactionId);
        List<TransactionParent> transactions = transactionParentRepository.findByRouterType(layoutOutputTransaction.getTransaction().getTransactionType());
        List<ComboBoxDto> dtos = new ArrayList<>();
        for (TransactionParent t : transactions) {
            dtos.add(new ComboBoxDto(t.getLayoutOutputTransactionId(), t.getIdentification()));
        }
        return dtos;
    }
    
    @Override
    public ExerciseMassDto execute(ExerciseMassDto dto) throws Exception {
        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }
        LayoutOutputTransaction layoutOutputTransaction = layoutOutputTransactionRepository.findOne(dto.getLayoutOutputTransactionId());
        dto.setOutputList(getOutputList(dto.getLayoutOutputTransactionId()));
        dto.setRouterOutputList(getOutputList(dto.getTransactionRouterId()));
        
        TransactionType transactionType = layoutOutputTransaction.getTransaction().getTransactionType();
        String transactionName = getTransactionName(layoutOutputTransaction.getTransaction());
        
        if (dto.getTransactionMechanismId().equals(Mechanism.ISD.getId())) {
            isdSimulator.executeTransaction(dto, transactionName, transactionType);
        } else if (dto.getTransactionMechanismId().equals(Mechanism.SERVTRANS.getId())) {
            servTranSimulator.executeTransaction(dto, transactionName, transactionType);
        } else if (dto.getTransactionMechanismId().equals(Mechanism.MAINFRAME.getId())) {
            //TODO: identificar o componente
            isdSimulator.executeTransaction(dto, transactionName, transactionType);
        }
        
        return dto;
    }
    
    private String getTransactionName(Transaction transaction) {
        if (transaction.getTransactionParent() == null) {
            return transaction.getIdentification();
        }
        return getTransactionName(transaction.getTransactionParent());
    }
    
    private List<ExerciseMassItemDto> getOutputList(Long layoutOutputTransactionId) {
        // Monta a lista de campos de saída
        List<ExerciseMassItemDto> dtos = new ArrayList<>();
        List<LayoutOutput> entities = layoutOutputRepository.findByLayoutOutputTransactionIdOrderByOrdinal(layoutOutputTransactionId);

        for (LayoutOutput input : entities) {
            dtos.add(new ExerciseMassItemDto(input.getId(), input.getName(), input.getSize(),
                input.getFieldType().getName(), null, input.getOrdinal(), false));
        }

        return dtos;
    }
}
