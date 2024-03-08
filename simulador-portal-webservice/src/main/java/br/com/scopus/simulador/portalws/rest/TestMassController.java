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
import br.com.scopus.simulador.business.service.TestMassService;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.TestMassDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PagedSearch;


/**
 * Servicos rest para teste de massa.
 * 
 * @author Yuslley Silva Fagundes - yfagundes@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_TEST_MASS")
@RequestMapping(path = RESTServices.CONTROLLER_TEST_MASS)

public class TestMassController {
    
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TestMassService testMassService;

    /**
     * Persiste a massa de teste passada como parametro.
     * 
     */
        
    @RequestMapping(path = RESTServices.REST_SAVE, method = RequestMethod.POST)
    public ResponseDto<TestMassDto> save(@RequestBody TestMassDto dto) {
        try {

            dto = this.testMassService.save(dto);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<TestMassDto>(e.getCode(), e.getMessage(), dto);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, dto);
        }
    }
    
    /**
     * Persiste o projeto passado como parametro.
     * 
     * @param dto
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_DELETE_DTO, method = RequestMethod.POST)
    public ResponseDto<TestMassDto> delete(@RequestBody TestMassDto dto) {
        try {
            this.testMassService.delete(dto);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, null);
       } catch (ValidationException e) {
            return new ResponseDto<TestMassDto>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, null);
        }
    }
    
    /**
     * Retorna a massa de teste que possui o id informado como parametro.
     * 
     * @param id
     * @return ResponseDto<TestMassDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_BY_ID, method = RequestMethod.GET)
    public ResponseDto<TestMassDto> findById(@PathVariable("id") Long id) {
        try {
            
            TestMassDto dto = this.testMassService.findById(id);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<TestMassDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, null);
            
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, null);
        }
    }
    
    @RequestMapping(path = RESTServices.REST_SEARCH, method = RequestMethod.POST)
    public ResponseDto<PageDto<TestMassDto>> search(@RequestBody PagedSearch<TestMassDto> search) {
        try {

            PageDto<TestMassDto> page = this.testMassService.search(search);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<PageDto<TestMassDto>> result = new ResponseDto<PageDto<TestMassDto>>(
                returnCode.getCode(), msg, page);
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<PageDto<TestMassDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<TestMassDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<TestMassDto>>(returnCode.getCode(), msg, null);
        }
    }
    
    @RequestMapping(path = RESTServices.REST_FIND_TRANSACTION_FIELDS, method = RequestMethod.POST)
    public ResponseDto<TestMassDto> findTransactionFields(@RequestBody TestMassDto dto) {
        try {

            dto.setInputList(this.testMassService.getInputList(dto.getId(), null, dto.getLayoutOutputTransactionId()));
            dto.setOutputList(this.testMassService.getOutputList(dto.getId(), dto.getLayoutOutputTransactionId()));

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<TestMassDto> result = new ResponseDto<TestMassDto>(
                returnCode.getCode(), msg, dto);
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<TestMassDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestMassDto>(returnCode.getCode(), msg, null);
        }
    }
        
}
