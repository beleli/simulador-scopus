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
import br.com.scopus.simulador.dto.ProjectDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;
import br.com.scopus.simulador.portal.util.Constants;

/**
 * Usuario controller.
 *
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_PROJECT")
@RequestMapping(path = Constants.CONTROLLER_PROJECT)
public class ProjectController extends SimuladorController<Object> {

    @Autowired
    ApplicationConfigRestService appConfigRestService;

    @RequestMapping(path = Constants.REST_CONSULTA, method = RequestMethod.POST)
    public ResponseDto<PageDto<ProjectDto>> search(@RequestBody PagedSearch<ProjectDto> pagedDto) {
        return chamadaPostWebService(pagedDto, new TypeToken<ResponseDto<PageDto<ProjectDto>>>() {
        }, appConfigRestService.getProjectByProjectWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CADASTRO_PROJETO, method = RequestMethod.POST)
    public ResponseDto<ProjectDto> save(@RequestBody ProjectDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<ProjectDto>>() {
        }, appConfigRestService.getSaveProjectWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_PROJETO_ID, method = RequestMethod.GET)
    public ResponseDto<ProjectDto> findById(@PathVariable("id") Long id) {
        return chamadaGetWebService(id, new TypeToken<ResponseDto<ProjectDto>>() {
        }, appConfigRestService.getProjectFindByIdWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_EXCLUIR_PROJETO_ID, method = RequestMethod.GET)
    public ResponseDto<ProjectDto> delete(@PathVariable("id") Long id) {
        return chamadaGetWebService(id, new TypeToken<ResponseDto<ProjectDto>>() {
        }, appConfigRestService.getProjectDeleteWebServiceURL());
    }
}
