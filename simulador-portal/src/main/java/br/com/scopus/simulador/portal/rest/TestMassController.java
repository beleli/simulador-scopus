package br.com.scopus.simulador.portal.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;

import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.TestMassDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;
import br.com.scopus.simulador.portal.util.Constants;

@RestController
@Secured("ROLE_VIEW_TEST_MASS")
@RequestMapping(path = Constants.CONTROLLER_TEST_MASS)
public class TestMassController extends SimuladorController<Object>{

    @Autowired
    ApplicationConfigRestService appConfigRestService;

    @RequestMapping(path = Constants.REST_CADASTRO_MASSA_TESTE, method = RequestMethod.POST)
    public ResponseDto<TestMassDto> save(@RequestBody TestMassDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<TestMassDto>>() {
        }, appConfigRestService.getTestMassSave());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_MASSA_TESTE_ID, method = RequestMethod.GET)
    public ResponseDto<TestMassDto> findById(@PathVariable("id") Long id) {
        return chamadaGetWebService(id, new TypeToken<ResponseDto<TestMassDto>>() {
        }, appConfigRestService.getTestMassFindById());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_MASSA_TESTE, method = RequestMethod.POST)
    public ResponseDto<PageDto<TestMassDto>> search(@RequestBody PagedSearch<TestMassDto> pagedDto) {
        return chamadaPostWebService(pagedDto, new TypeToken<ResponseDto<PageDto<TestMassDto>>>() {
        }, appConfigRestService.getTestMassFindByTestMass());
    }
    
    @RequestMapping(path = Constants.REST_EXCLUIR_MASSA_TESTE, method = RequestMethod.POST)
    public ResponseDto<TestMassDto> delete(@RequestBody TestMassDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<TestMassDto>>() {
        }, appConfigRestService.getTestMassDelete());
    }
    
    @RequestMapping(path = Constants.REST_CONSULTA_TRANSACOES_MASSA, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findMassTransactions(@PathVariable("identification") String identification) {
        return chamadaGetWebService(identification, new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getFindMassTransactionsWebSerivceURL());
    }
    
    @RequestMapping(path = Constants.REST_CONSULTA_COMBO_PROJETO, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> consultaComboProjeto() {
        return chamadaGetWebService(new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getComboProjectWebServiceURL());
    }
    
    @RequestMapping(path = Constants.REST_CONSULTA_COMBO_CENARIO, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findByTransactionId(@PathVariable("transactionId") Long transactionId) {
        return chamadaGetWebService(transactionId, new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getTestScenarioFindByTransaction());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_CAMPOS_MASSA, method = RequestMethod.POST)
    public ResponseDto<TestMassDto> findFieldsByTransaction(@RequestBody TestMassDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<TestMassDto>>() {
        }, appConfigRestService.getTestMassFindFieldList());
    }

}
