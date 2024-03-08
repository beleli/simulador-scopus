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
import br.com.scopus.simulador.dto.TestScenarioDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;
import br.com.scopus.simulador.portal.util.Constants;

/**
 * Cenario de Teste controller.
 *
 * @author Yuslley Silva Fagundes - yfagundes@scopus.com.br
 * @since 1.0
 */

@RestController
@Secured("ROLE_VIEW_TEST_SCENARIO")
@RequestMapping(path = Constants.CONTROLLER_TEST_SCENARIO)
public class TestScenarioController extends SimuladorController<Object> {

    @Autowired
    ApplicationConfigRestService appConfigRestService;

    @RequestMapping(path = Constants.REST_CONSULTA, method = RequestMethod.POST)
    public ResponseDto<PageDto<TestScenarioDto>> search(@RequestBody PagedSearch<TestScenarioDto> pagedDto) {
        return chamadaPostWebService(pagedDto, new TypeToken<ResponseDto<PageDto<TestScenarioDto>>>() {
        }, appConfigRestService.getTestScenarioSearch());
    }

    @RequestMapping(path = Constants.REST_CADASTRO_CENARIO_TESTE, method = RequestMethod.POST)
    public ResponseDto<TestScenarioDto> save(@RequestBody TestScenarioDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<TestScenarioDto>>() {
        }, appConfigRestService.getTestScenarioSave());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_CENARIO_TESTE_ID, method = RequestMethod.GET)
    public ResponseDto<TestScenarioDto> findById(@PathVariable("id") Long id) {
        return chamadaGetWebService(id, new TypeToken<ResponseDto<TestScenarioDto>>() {
        }, appConfigRestService.getTestScenarioFindById());
    }

    @RequestMapping(path = Constants.REST_DELETE, method = RequestMethod.GET)
    public ResponseDto<TestScenarioDto> delete(@PathVariable("id") Long id) {
        return chamadaGetWebService(id, new TypeToken<ResponseDto<TestScenarioDto>>() {
        }, appConfigRestService.getTestScenarioDelete());
    }

    @RequestMapping(path = Constants.REST_FIND_SCENARIOS_TRANSACTIONS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findScenarioTransactions(@PathVariable("identification") String identification) {
        return chamadaGetWebService(identification, new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getFindScenarioTransactionsWebSerivceURL());
    }
}
