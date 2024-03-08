package br.com.scopus.simulador.portalws.rest;

import java.util.List;

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
import br.com.scopus.simulador.business.service.ExerciseMassService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.ExerciseMassDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;

/**
 * Servicos rest para exercitar massa de teste.
 * 
 * @author Carlos Alberto Beleli Junior - yfagundes@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_EXERCISE_MASS")
@RequestMapping(path = RESTServices.CONTROLLER_EXERCISE_MASS)
public class ExerciseMassController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ExerciseMassService exerciseMassService;
    
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTION_FIELDS, method = RequestMethod.POST)
    public ResponseDto<ExerciseMassDto> findTransactionFields(@RequestBody ExerciseMassDto dto) {
        try {
            dto.setInputList(this.exerciseMassService.getInputList(dto.getLayoutOutputTransactionId()));
            
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<ExerciseMassDto> result = new ResponseDto<ExerciseMassDto>(
                returnCode.getCode(), msg, dto);
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<ExerciseMassDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ExerciseMassDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ExerciseMassDto>(returnCode.getCode(), msg, null);
        }
    }
    
    /**
     * Recupera comboBox de projetos ativos
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTION_MECHANISMS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> getComboBoxMechanisms() {
        try {
            List<ComboBoxDto> users = this.exerciseMassService.getComboBoxMechanisms();
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
     * Recupera comboBox de projetos ativos
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTION_ROUTER, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> getComboBoxRouters(@PathVariable("layoutOutputTransactionId") Long layoutOutputTransactionId) {
        try {
            List<ComboBoxDto> transactions = this.exerciseMassService.getComboBoxRouters(layoutOutputTransactionId);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, transactions);
        } catch (ValidationException e) {
            return new ResponseDto<List<ComboBoxDto>>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<List<ComboBoxDto>>(returnCode.getCode(), msg, null);
        }
    }

    @RequestMapping(path = RESTServices.REST_FIND_ROUTER_FIELDS, method = RequestMethod.POST)
    public ResponseDto<ExerciseMassDto> findRouterFields(@RequestBody ExerciseMassDto dto) {
        try {
            dto.setRouterInputList(this.exerciseMassService.getInputList(dto.getTransactionRouterId()));
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<ExerciseMassDto> result = new ResponseDto<ExerciseMassDto>(
                returnCode.getCode(), msg, dto);
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<ExerciseMassDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ExerciseMassDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ExerciseMassDto>(returnCode.getCode(), msg, null);
        }
    }    
    
    @RequestMapping(path = RESTServices.REST_EXECUTE, method = RequestMethod.POST)
    public ResponseDto<ExerciseMassDto> execute(@RequestBody ExerciseMassDto dto) {
        try {
            ExerciseMassDto newDto = this.exerciseMassService.execute(dto);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<ExerciseMassDto> result = new ResponseDto<ExerciseMassDto>(returnCode.getCode(), msg, newDto);
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<ExerciseMassDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ExerciseMassDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ExerciseMassDto>(returnCode.getCode(), msg, null);
        }
    }
}
