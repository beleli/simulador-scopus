package br.com.scopus.simulador.portalws.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.scopus.simulador.business.service.TransactionService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.TestMassDto;
import br.com.scopus.simulador.dto.TransactionDto;
import br.com.scopus.simulador.dto.TransactionItemDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PagedSearch;

/**
 * Servicos rest para usuarios.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_TRANSACTION")
@RequestMapping(path = RESTServices.CONTROLLER_TRANSACTION)
public class TransactionController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TransactionService transactionService;

    /**
     * Persiste a transação passado como parametro.
     * 
     * @param dto
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SAVE, method = RequestMethod.POST)
    public ResponseDto<TransactionDto> save(@RequestBody TransactionDto dto) {
        try {

            dto = this.transactionService.save(dto);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TransactionDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<TransactionDto>(e.getCode(), e.getMessage(), dto);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TransactionDto>(returnCode.getCode(), msg, dto);
        }
    }


    /**
     * Busca a transação por nome.
     * 
     * @param dto
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_BY_NAME, method = RequestMethod.POST)
    public ResponseDto<List<TransactionDto>> findByDescription(@RequestBody TransactionDto dto) {

        List<TransactionDto> listDto = new ArrayList<TransactionDto>();

        try {

            if (dto.getTransactionTypeId() == null || dto.getTransactionTypeId() == 0)
                listDto = this.transactionService.findByIdentification(dto.getName());
            else
                listDto = this.transactionService.findByIdentificationAndType(dto.getName(),
                    dto.getTransactionTypeId());
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<TransactionDto>>(returnCode.getCode(), msg, listDto);

        } catch (ValidationException e) {
            return new ResponseDto<List<TransactionDto>>(e.getCode(), e.getMessage(), listDto);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<TransactionDto>>(returnCode.getCode(), msg, listDto);
        }
    }


    /**
     * Retorna o projeto que possui o id informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTION, method = RequestMethod.POST)
    public ResponseDto<TransactionDto> findById(@RequestBody TransactionDto dto) {
        try {

            dto = this.transactionService.getDtoByIdAndIdOutPut(dto);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TransactionDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<TransactionDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TransactionDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TransactionDto>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Retorna os projetos que atendem ao projeto informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SEARCH, method = RequestMethod.POST)
    public ResponseDto<PageDto<TransactionDto>> search(@RequestBody PagedSearch<TransactionDto> search) {
        try {

            PageDto<TransactionDto> page = this.transactionService.findByTransaction(search);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<PageDto<TransactionDto>> result = new ResponseDto<PageDto<TransactionDto>>(returnCode.getCode(),
                msg, page);
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<PageDto<TransactionDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<TransactionDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<TransactionDto>>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Retorna os tipos de transações.
     * 
     * @return ResponseDto<List<ComboBoxDto>>
     */
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTION_TYPES, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findTransactionTypes() {
        try {
            List<ComboBoxDto> lista = this.transactionService.getComboBoxTransactionType();
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, lista);
        } catch (ValidationException e) {
            return new ResponseDto<List<ComboBoxDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Retorna os tipos de codificação utilizados nas transações.
     * 
     * @return ResponseDto<List<ComboBoxDto>>
     */
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTION_FORMATS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findTransactionFormats() {
        try {
            List<ComboBoxDto> lista = this.transactionService.getComboBoxTransactionFormats();
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, lista);
        } catch (ValidationException e) {
            return new ResponseDto<List<ComboBoxDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Recupera comboBox de usuario
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_FIELD_TYPES, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> getComboBoxLayoutFieldType() {
        try {

            List<ComboBoxDto> users = this.transactionService.getComboBoxLayoutFieldType();
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, users);

        } catch (ValidationException e) {
            return new ResponseDto<List<ComboBoxDto>>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Recupera comboBox de usuario
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_PARENTS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> getComboBoxTransactionParent() {
        try {

            List<ComboBoxDto> users = this.transactionService.getComboBoxTransactionParent();
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, users);

        } catch (ValidationException e) {
            return new ResponseDto<List<ComboBoxDto>>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        }
    }

    @RequestMapping(path = RESTServices.REST_GET_PARENT_FIELDS, method = RequestMethod.POST)
    public ResponseDto<List<TransactionItemDto>> getParentFields(@RequestBody TransactionDto dto) {
        try {

            List<TransactionItemDto> page = this.transactionService.getLayoutInput(dto.getId(),
                dto.getTransactionOutputId(), true);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<List<TransactionItemDto>> result = new ResponseDto<List<TransactionItemDto>>(
                returnCode.getCode(), msg, page);
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<List<TransactionItemDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<TransactionItemDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<TransactionItemDto>>(returnCode.getCode(), msg, null);
        }
    }
    
    /**
     * Recupera comboBox de usuario
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTIONS_SCENARIO, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> getComboBoxTransactionsScenario(@PathVariable("identification") String identification) {
        try {
            List<ComboBoxDto> transactions = this.transactionService.getComboBoxScenarioTranactions(identification);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, transactions);
        } catch (ValidationException e) {
            return new ResponseDto<List<ComboBoxDto>>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        }
    }
    
    /**
     * Recupera comboBox de usuario
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTIONS_MASS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> getComboBoxTransactionsMass(@PathVariable("identification") String identification) {
        try {
            List<ComboBoxDto> transactions = this.transactionService.getComboBoxTestMassTranactions(identification);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, transactions);
        } catch (ValidationException e) {
            return new ResponseDto<List<ComboBoxDto>>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        }
    }
    
    /**
     * Deleta a transação passada como parametro.
     * 
     * @param dto
     * @return ResponseDto<TestMassDto>
     */
    @RequestMapping(path = RESTServices.REST_DELETE_DTO, method = RequestMethod.POST)
    public ResponseDto<TestMassDto> delete(@RequestBody TransactionDto dto) {
        try {
            this.transactionService.deleteDto(dto);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, null);
       } catch (ValidationException e) {
            return new ResponseDto<TestMassDto>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, null);
        }
    }
    
}
