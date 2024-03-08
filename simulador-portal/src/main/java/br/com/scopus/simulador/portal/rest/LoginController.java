package br.com.scopus.simulador.portal.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;

import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.UserDto;
import br.com.scopus.simulador.portal.auth.UsuarioLogado;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;
import br.com.scopus.simulador.portal.util.Constants;

/**
 * Login controller.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@RestController
@RequestMapping(path = Constants.CONTROLLER_LOGIN)
public class LoginController extends SimuladorController<Object> {

    @Autowired
    ApplicationConfigRestService appConfigRestService;
    
    @RequestMapping(path = Constants.REST_USER, method = RequestMethod.GET)
    public UsuarioLogado user(Principal user) {
        return (UsuarioLogado) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
    }
    
    @RequestMapping(path = Constants.REST_RECUPERAR_SENHA, method = RequestMethod.GET)
    public ResponseDto<UserDto> recoverPassword(@PathVariable("email") String email) {
        return chamadaGetWebService(email, new TypeToken<ResponseDto<UserDto>>() {
        }, appConfigRestService.getUserRecoverPasswordWebSerivceURL());
    }
    
}
