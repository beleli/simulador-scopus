package br.com.scopus.simulador.portalws.rest;

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
import br.com.scopus.simulador.business.service.ConfigurationService;
import br.com.scopus.simulador.dto.ConfigurationDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PagedSearch;

/**
 * Servicos rest para configuração.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_CONFIGURATION")
@RequestMapping(path = RESTServices.CONTROLLER_CONFIGURATION)
public class ConfigurationController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ConfigurationService configurationService;

    /**
     * Persiste a configuração passada como parametro.
     * 
     * @param dto
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SAVE, method = RequestMethod.POST)
    public ResponseDto<ConfigurationDto> save(@RequestBody ConfigurationDto dto) {
        try {
            dto = this.configurationService.save(dto);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ConfigurationDto>(returnCode.getCode(), msg, dto);
        } catch (ValidationException e) {
            return new ResponseDto<ConfigurationDto>(e.getCode(), e.getMessage(), dto);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ConfigurationDto>(returnCode.getCode(), msg, dto);
        }
    }
    
    /**
     * Persiste a configuração passada como parametro.
     * 
     * @param dto
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_DELETE, method = RequestMethod.GET)
    public ResponseDto<ConfigurationDto> delete(@PathVariable("id") Long id) {
        try {
            this.configurationService.deleteDtoById(id);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ConfigurationDto>(returnCode.getCode(), msg, null);
       } catch (ValidationException e) {
            return new ResponseDto<ConfigurationDto>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ConfigurationDto>(returnCode.getCode(), msg, null);
        }
    }
        
    /**
     * Retorna a configuração que possui o id informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_BY_ID, method = RequestMethod.GET)
    public ResponseDto<ConfigurationDto> findById(@PathVariable("id") Long id) {
        try {
            ConfigurationDto dto = this.configurationService.getDtoById(id);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ConfigurationDto>(returnCode.getCode(), msg, dto);
        } catch (ValidationException e) {
            return new ResponseDto<ConfigurationDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ConfigurationDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ConfigurationDto>(returnCode.getCode(), msg, null);
        }
    }
    
    /**
     * Retorna as configurações que atendem a configuração informada como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SEARCH, method = RequestMethod.POST)
    public ResponseDto<PageDto<ConfigurationDto>> search(@RequestBody PagedSearch<ConfigurationDto> search) {
        try {
            PageDto<ConfigurationDto> page = this.configurationService.search(search);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<PageDto<ConfigurationDto>> result = new ResponseDto<PageDto<ConfigurationDto>>(returnCode.getCode(), msg, page); 
            return result;
        } catch (ValidationException e) {
            return new ResponseDto<PageDto<ConfigurationDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<ConfigurationDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<ConfigurationDto>>(returnCode.getCode(), msg, null);
        }
    }
}
