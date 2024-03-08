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
import br.com.scopus.simulador.business.service.TestScenarioService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.TestScenarioDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PagedSearch;

@RestController
@Secured("ROLE_VIEW_TEST_SCENARIO")
@RequestMapping(path = RESTServices.CONTROLLER_TEST_SCENARIO)
public class TestScenarioController {


    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TestScenarioService testScnearioService;

    /**
     * Persiste a transação passado como parametro.
     * 
     * @param dto
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SAVE, method = RequestMethod.POST)
    public ResponseDto<TestScenarioDto> save(@RequestBody TestScenarioDto dto) {
        try {

            dto = this.testScnearioService.save(dto);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestScenarioDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<TestScenarioDto>(e.getCode(), e.getMessage(), dto);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestScenarioDto>(returnCode.getCode(), msg, dto);
        }
    }


    /**
     * Retorna o projeto que possui o id informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_BY_ID, method = RequestMethod.GET)
    public ResponseDto<TestScenarioDto> findById(@PathVariable("id") Long id) {
        try {

            TestScenarioDto dto = this.testScnearioService.getDtoById(id);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestScenarioDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<TestScenarioDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestScenarioDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestScenarioDto>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Retorna o projeto que possui o id informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_DELETE, method = RequestMethod.GET)
    public ResponseDto<TestScenarioDto> delete(@PathVariable("id") Long id) {
        try {

            TestScenarioDto dto = this.testScnearioService.deleteDtoById(id);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestScenarioDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<TestScenarioDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestScenarioDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<TestScenarioDto>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Retorna os projetos que atendem ao projeto informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SEARCH, method = RequestMethod.POST)
    public ResponseDto<PageDto<TestScenarioDto>> search(@RequestBody PagedSearch<TestScenarioDto> search) {
        try {

            PageDto<TestScenarioDto> page = this.testScnearioService.findByTransactionId(search);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<PageDto<TestScenarioDto>> result = new ResponseDto<PageDto<TestScenarioDto>>(
                returnCode.getCode(), msg, page);
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<PageDto<TestScenarioDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<TestScenarioDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<TestScenarioDto>>(returnCode.getCode(), msg, null);
        }
    }

    /**
     * Recupera comboBox de cenários de acordo com a saída
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_SCENARIO_MASS, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> getComboBoxTestScenario(
        @PathVariable("layoutOutputTransactionId") Long layoutOutputTransactionId) {
        try {

            List<ComboBoxDto> users = this.testScnearioService.getComboBoxTestScenario(layoutOutputTransactionId);
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
}
