package br.com.scopus.simulador.portal.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;

import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ConfigurationDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;
import br.com.scopus.simulador.portal.util.Constants;

/**
 * Configuration Controller.
 *
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_CONFIGURATION")
@RequestMapping(path = Constants.CONTROLLER_CONFIGURATION)
public class ConfigurationController extends SimuladorController<Object> {

    @Autowired
    ApplicationConfigRestService appConfigRestService;

    @RequestMapping(path = Constants.REST_CONSULTA, method = RequestMethod.POST)
    public ResponseDto<PageDto<ConfigurationDto>> search(@RequestBody PagedSearch<ConfigurationDto> pagedDto) {
        return chamadaPostWebService(pagedDto, new TypeToken<ResponseDto<PageDto<ConfigurationDto>>>() {
        }, appConfigRestService.getConfigurationByConfigurationWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CADASTRO_CONFIGURACAO, method = RequestMethod.POST)
    public ResponseDto<ConfigurationDto> save(@RequestBody ConfigurationDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<ConfigurationDto>>() {
        }, appConfigRestService.getSaveConfigurationWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_CONFIGURACAO_ID, method = RequestMethod.GET)
    public ResponseDto<ConfigurationDto> findById(@PathVariable("id") Long id) {
        return chamadaGetWebService(id, new TypeToken<ResponseDto<ConfigurationDto>>() {
        }, appConfigRestService.getConfigurationFindByIdWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_EXCLUIR_CONFIGURACAO_ID, method = RequestMethod.GET)
    public ResponseDto<ConfigurationDto> delete(@PathVariable("id") Long id) {
        return chamadaGetWebService(id, new TypeToken<ResponseDto<ConfigurationDto>>() {
        }, appConfigRestService.getConfigurationDeleteWebServiceURL());
    }
}
