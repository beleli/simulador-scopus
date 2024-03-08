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

import br.com.scopus.simulador.dto.AlterarSenhaDto;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.UserDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;
import br.com.scopus.simulador.portal.util.Constants;

/**
 * Usuario controller.
 * 
 * @author Eduardo Luiz Pelorca - epelorca@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_USER")
@RequestMapping(path = Constants.CONTROLLER_USUARIO)
public class UsuarioController extends SimuladorController<Object> {

    @Autowired
    ApplicationConfigRestService appConfigRestService;

    @RequestMapping(path = Constants.REST_CADASTRO_USUARIO, method = RequestMethod.POST)
    public ResponseDto<UserDto> cadastroUsuario(@RequestBody UserDto user) {
        return chamadaPostWebService(user, new TypeToken<ResponseDto<UserDto>>() {
        }, appConfigRestService.getSaveUserWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_USUARIO_ID, method = RequestMethod.GET)
    public ResponseDto<UserDto> findById(@PathVariable("id") Long id) {
        return chamadaGetWebService(id, new TypeToken<ResponseDto<UserDto>>() {
        }, appConfigRestService.getUserFindByIdWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_USUARIO_EMAIL, method = RequestMethod.GET)
    public ResponseDto<UserDto> findByEmail(@PathVariable("email") String email) {
        return chamadaGetWebService(email, new TypeToken<ResponseDto<UserDto>>() {
        }, appConfigRestService.getUserFindByEmailWebSerivceURL());
    }
    
    @Secured("ROLE_VIEW_CHANGE_PASSWORD")
    @RequestMapping(path = Constants.REST_ALTERAR_SENHA, method = RequestMethod.POST)
    public ResponseDto<UserDto> alterarSenha(@RequestBody AlterarSenhaDto dto) {
        return chamadaPostWebService(dto, new TypeToken<ResponseDto<UserDto>>() {
        }, appConfigRestService.getUserChangePasswordWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_COMBO_USUARIO, method = RequestMethod.POST)
    public ResponseDto<List<ComboBoxDto>> consultaComboUsuario(@RequestBody String nome) {
        return chamadaPostWebService(nome, new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
        }, appConfigRestService.getUserFindByNameWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_PERFIS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findProfiles() {
            return chamadaGetWebService(new TypeToken<ResponseDto<List<ComboBoxDto>>>() {
            }, appConfigRestService.getUserFindProfileWebSerivceURL());
    }

    @RequestMapping(path = Constants.REST_CONSULTA_USUARIO, method = RequestMethod.POST)
    public ResponseDto<PageDto<UserDto>> findByUser(@RequestBody PagedSearch<UserDto> pagedDto) {
            return chamadaPostWebService(pagedDto, new TypeToken<ResponseDto<PageDto<UserDto>>>() {
            }, appConfigRestService.getUserFindByUserWebSerivceURL());
    }

}
