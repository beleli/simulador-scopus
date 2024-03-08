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
import br.com.scopus.simulador.dto.ExerciseMassDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;
import br.com.scopus.simulador.portal.util.Constants;

@RestController
@Secured("ROLE_VIEW_EXERCISE_MASS")
@RequestMapping(path = Constants.CONTROLLER_EXERCISE_MASS)
public class ExerciseMassController extends SimuladorController<Object>{

    @Autowired
    ApplicationConfigRestService appConfigRestService;

    @RequestMapping(path = Constants.REST_CONSULTA_CAMPOS_ENTRADA, method = RequestMethod.POST)
    public ResponseDto<ExerciseMassDto> findFieldsByTransaction(@RequestBody ExerciseMassDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<ExerciseMassDto>>() {
        }, appConfigRestService.getExerciseMassFindInputListWebSerivceURL());
    }   
    
    @RequestMapping(path = Constants.REST_CONSULTA_CAMPOS_ROTEADORA, method = RequestMethod.POST)
    public ResponseDto<ExerciseMassDto> findRouterListByTransaction(@RequestBody ExerciseMassDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<ExerciseMassDto>>() {
        }, appConfigRestService.getExerciseMassFindRouterListWebSerivceURL());
    }
    
    @RequestMapping(path = Constants.REST_CONSULTA_COMBO_MECANISMO, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> consultaComboMecanismo() {
        return chamadaGetWebService(new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getComboMechanismWebServiceURL());
    }
    
    @RequestMapping(path = Constants.REST_CONSULTA_COMBO_ROTEADORAS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findByOutputTransactionId(@PathVariable("layoutOutputTransactionId") Long layoutOutputTransactionId) {
        return chamadaGetWebService(layoutOutputTransactionId, new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getRoutingTransactionsWebSerivceURL());
    }
    
    @RequestMapping(path = Constants.REST_EXECUTA_TRANSACAO, method = RequestMethod.POST)
    public ResponseDto<ExerciseMassDto> execute(@RequestBody ExerciseMassDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<ExerciseMassDto>>() {
        }, appConfigRestService.getExecuteTransactionWebSerivceURL());
    }
}
