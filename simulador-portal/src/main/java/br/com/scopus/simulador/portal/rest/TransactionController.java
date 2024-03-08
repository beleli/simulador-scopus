package br.com.scopus.simulador.portal.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;

import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.TransactionDto;
import br.com.scopus.simulador.dto.TransactionItemDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;
import br.com.scopus.simulador.portal.util.Constants;

/**
 * Usuario controller.
 *
 * @author Yuslley Silva Fagundes - yfagundes@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_TRANSACTION")
@RequestMapping(path = Constants.CONTROLLER_TRANSACTION)
public class TransactionController extends SimuladorController<Object> {

    @Autowired
    ApplicationConfigRestService appConfigRestService;

    @RequestMapping(path = Constants.REST_CONSULTA, method = RequestMethod.POST)
    public ResponseDto<PageDto<TransactionDto>> search(@RequestBody PagedSearch<TransactionDto> pagedDto) {
        return chamadaPostWebService(pagedDto, new TypeToken<ResponseDto<PageDto<TransactionDto>>>() {
        }, appConfigRestService.getTransactionByTransactionWebServiceURL());
    }


    @RequestMapping(path = Constants.REST_CONSULTA_TRANSACAO_BY_TRANSACTION, method = RequestMethod.POST)
    public ResponseDto<TransactionDto> findTransactionByTransactionView(@RequestBody TransactionDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<TransactionDto>>() {
        }, appConfigRestService.getTransactionByTransactionViewWebServiceURL());
    }


    @RequestMapping(path = Constants.REST_FIND_TRANSACTION_BY_NAME, method = RequestMethod.POST)
    public ResponseDto<List<TransactionDto>> findByName(@RequestBody TransactionDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<List<TransactionDto>>>() {
        }, appConfigRestService.getFindTransactionByName());
    }


    @RequestMapping(path = Constants.REST_CADASTRO_TRANSACAO, method = RequestMethod.POST)
    public ResponseDto<PageDto<TransactionDto>> save(@RequestBody TransactionDto pagedDto) {
        return chamadaPostWebService(pagedDto, new TypeToken<ResponseDto<PageDto<TransactionDto>>>() {
        }, appConfigRestService.getSaveTransactionWebServiceURL());
    }

    @RequestMapping(path = Constants.REST_FIND_TRANSACTION_TYPES, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findTransactionTypes() {
        return chamadaGetWebService(new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getFindTransactionTypes());
    }

    @RequestMapping(path = Constants.REST_FIND_TRANSACTION_FORMATS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findTransactionFormats() {
        return chamadaGetWebService(new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getFindTransactionFormats());
    }

    @RequestMapping(path = Constants.REST_FIND_FIELDS_TYPE, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findFieldsType() {
        return chamadaGetWebService(new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getFindFieldsType());
    }

    @RequestMapping(path = Constants.REST_FIND_PARENTS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findTransactionParents() {
        return chamadaGetWebService(new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getFindParents());
    }

    @RequestMapping(path = Constants.REST_FIND_PARENT_FIELDS, method = RequestMethod.POST)
    public ResponseDto<List<TransactionItemDto>> findInputFields(@RequestBody TransactionDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<List<TransactionItemDto>>>() {
        }, appConfigRestService.getFindParentFieldsWebServiceURL());
    }
    
    @RequestMapping(path = Constants.REST_EXCLUIR_TRANSCAO, method = RequestMethod.POST)
    public ResponseDto<TransactionDto> delete(@RequestBody TransactionDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<TransactionDto>>() {
        }, appConfigRestService.getTransactionDeleteWebServiceURL());
    }

}
