package br.com.scopus.simulador.portalws.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.scopus.simulador.business.service.UserService;
import br.com.scopus.simulador.dto.AlterarSenhaDto;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.RoleUserDto;
import br.com.scopus.simulador.dto.UserDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PagedSearch;

/**
 * Servicos rest para usuarios.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@RestController
@RequestMapping(path = RESTServices.CONTROLLER_USER)
public class UserController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    /**
     * Persiste o usuario passado como parametro.
     * 
     * @param user
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SAVE, method = RequestMethod.POST)
    public ResponseDto<UserDto> save(@RequestBody UserDto user) {
        try {

            user = this.userService.salvarUsuario(user);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, user);

        } catch (ValidationException e) {
            return new ResponseDto<UserDto>(e.getCode(), e.getMessage(), user);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, user);
        }
    }

    /**
     * Retorna o usuario que possui o id informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_BY_ID, method = RequestMethod.GET)
    public ResponseDto<UserDto> findById(@PathVariable("id") Long id) {
        try {

            UserDto dto = this.userService.getDtoById(id);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<UserDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Retorna o usuario que possui o email informado como parametro.
     * 
     * @param email
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_BY_EMAIL, method = RequestMethod.GET)
    public ResponseDto<UserDto> findByEmail(@PathVariable("email") String email) {
        try {

            UserDto dto = this.userService.findByEmail(email);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<UserDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Retorna as roles do usuário recebendo como parâmetro o id do Perfil.
     * 
     * @param idProfile
     * @return ResponseDto<RoleUserDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_ROLE_USER_BY_PROFILE, method = RequestMethod.GET)
    public ResponseDto<RoleUserDto> findRoleUserByProfile(@PathVariable("idProfile") Integer idProfile) {
        try {
            List<String> lista = this.userService.consultarPermissoesPorIdPerfil(idProfile);
            RoleUserDto dto = new RoleUserDto(lista);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<RoleUserDto>(returnCode.getCode(), msg, dto);
        } catch (ValidationException e) {
            return new ResponseDto<RoleUserDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<RoleUserDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<RoleUserDto>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Invoca o serviço para alteração de senha.
     * 
     * @param params Informações de troca de senha.
     */
    @RequestMapping(path = RESTServices.REST_CHANGE_PASSWORD, method = RequestMethod.POST)
    public ResponseDto<AlterarSenhaDto> changePassword(@RequestBody AlterarSenhaDto params) {
        try {

            this.userService.alterarSenha(params);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<AlterarSenhaDto>(returnCode.getCode(), msg, params);

        } catch (ValidationException e) {
            return new ResponseDto<AlterarSenhaDto>(e.getCode(), e.getMessage(), params);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<AlterarSenhaDto>(returnCode.getCode(), msg, params);
        }
    }

    /**
     * Recupera comboBox de usuario
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_BY_NAME, method = RequestMethod.POST)
    public ResponseDto<List<ComboBoxDto>> findByName(@RequestBody String name) {
        try {

            List<ComboBoxDto> users = this.userService.consultarComboPorNome(name.replace("\"",""));
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
     * Retorna o usuario que possui o email informado como parametro.
     * 
     * @param email
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_GET_PROFILES, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> findProfiles() {
        try {

            List<ComboBoxDto> dto = this.userService.getComboBoxProfiles();

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, dto);

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

    @RequestMapping(path = RESTServices.REST_SEARCH, method = RequestMethod.POST)
    public ResponseDto<PageDto<UserDto>> search(@RequestBody PagedSearch<UserDto> search) {
        try {
            PageDto<UserDto> page = this.userService.findByUser(search);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<UserDto>>(returnCode.getCode(), msg, page);
        } catch (ValidationException e) {
            return new ResponseDto<PageDto<UserDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<UserDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<UserDto>>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Envia email com nova senha para o usuÃ¡rio
     * 
     * @param login
     * @return OperationReturn<UserDto>
     */
    @RequestMapping(path = RESTServices.REST_RECOVER_PASSWORD, method = RequestMethod.GET)
    public ResponseDto<UserDto> recoverPassword(@PathVariable("email") String email) {
        try {
            this.userService.recuperarSenha(email);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, null);
        } catch (ValidationException e) {
            return new ResponseDto<UserDto>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<UserDto>(returnCode.getCode(), msg, null);
        }
    }
    
}
