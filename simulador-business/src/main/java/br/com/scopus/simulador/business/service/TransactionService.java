package br.com.scopus.simulador.business.service;

import java.util.List;

import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.service.CrudService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.TransactionDto;
import br.com.scopus.simulador.dto.TransactionItemDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.entity.Transaction;

/**
 * Interface de servico para transações.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public interface TransactionService extends CrudService<TransactionDto, Transaction> {

    /***
     * Sava a transação.
     * 
     * @param dto
     * @return dto
     * @throws ValidationException
     * @throws ServiceException
     */
    TransactionDto save(TransactionDto dto) throws ValidationException, ServiceException;

    /***
     * Busca a transação de acordo com a idenficação.
     * 
     * @param search
     * @return
     */
    PageDto<TransactionDto> findByTransaction(PagedSearch<TransactionDto> search);

    /***
     * Retorna lista de tipos de transação suportada pelo sistema.
     * 
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> getComboBoxTransactionType();

    /***
     * Retorna lista de tipos de codificação suportada pelo sistema.
     * 
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> getComboBoxTransactionFormats();

    /***
     * Retorna lista de transações pai.
     * 
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> getComboBoxTransactionParent();

    /**
     * Retorno a lista de formato de campos
     * 
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> getComboBoxLayoutFieldType();


    /**
     * Busca as transações pela identificação
     * 
     * @param name
     * @return List<TransactionDto>
     */
    List<TransactionDto> findByIdentification(String name);

    /**
     * Busca as transações pela identificação e pelo tipo
     * 
     * @param name
     * @param type
     * @return List<TransactionDto>
     */
    List<TransactionDto> findByIdentificationAndType(String name, Integer type);

    /**
     * Busca a transação pelo Id e pelo layoutOutputTransaction
     * 
     * @param dto
     * @return TransactionDto
     */
    TransactionDto getDtoByIdAndIdOutPut(TransactionDto dto);

    /**
     * Busca a lista de campos de entrada da transação
     * 
     * @param transactionId
     * @param transactionOutputTransactionId
     * @param parent
     * @param recursive
     * @return List<TransactionItemDto>
     */
    List<TransactionItemDto> getLayoutInput(Long transactionId, Long transactionOutputTransactionId, boolean parent);

    /**
     * Busca a lista de campos de saída da transação
     * 
     * @param layoutOutputTransactionId
     * @return List<TransactionItemDto>
     */
    List<TransactionItemDto> getLayoutOutput(Long layoutOutputTransactionId);

    /**
     * Busca as transaçõse que podem ser usadas para cadastro de cenários de teste.
     * 
     * @param name
     * @return List<TransactionDto>
     */
    List<ComboBoxDto> getComboBoxScenarioTranactions(String identification);

    /**
     * Busca as transaçõse que podem ser usadas para cadastro de massa de testes.
     * 
     * @param name
     * @return List<TransactionDto>
     */
    List<ComboBoxDto> getComboBoxTestMassTranactions(String name);

    /**
     * Busca o id da transação pai de acordo com a saída.
     * 
     * @param layoutOutputTransactionId
     * @return
     */
    Long getTransactionParentByOutput(Long layoutOutputTransactionId);

    /**
     * Deleta a transação indicada como parametro.
     * 
     * @param dto
     * @return
     * @throws ValidationException
     * @throws ServiceException
     */
    TransactionDto deleteDto(TransactionDto dto) throws ValidationException, ServiceException;
}
