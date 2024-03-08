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
import br.com.scopus.simulador.business.service.ProjectService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.ProjectDto;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PagedSearch;

/**
 * Servicos rest para usuarios.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@RestController
@Secured("ROLE_VIEW_PROJECT")
@RequestMapping(path = RESTServices.CONTROLLER_PROJECT)
public class ProjectController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProjectService projectService;

    /**
     * Persiste o projeto passado como parametro.
     * 
     * @param dto
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SAVE, method = RequestMethod.POST)
    public ResponseDto<ProjectDto> save(@RequestBody ProjectDto dto) {
        try {

            dto = this.projectService.save(dto);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ProjectDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<ProjectDto>(e.getCode(), e.getMessage(), dto);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ProjectDto>(returnCode.getCode(), msg, dto);
        }
    }
    
    /**
     * Persiste o projeto passado como parametro.
     * 
     * @param dto
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_DELETE, method = RequestMethod.GET)
    public ResponseDto<ProjectDto> delete(@PathVariable("id") Long id) {
        try {
            this.projectService.delete(id);
            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ProjectDto>(returnCode.getCode(), msg, null);
       } catch (ValidationException e) {
            return new ResponseDto<ProjectDto>(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ProjectDto>(returnCode.getCode(), msg, null);
        }
    }
        

    /**
     * Retorna o projeto que possui o id informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_BY_ID, method = RequestMethod.GET)
    public ResponseDto<ProjectDto> findById(@PathVariable("id") Long id) {
        try {

            ProjectDto dto = this.projectService.getDtoById(id);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ProjectDto>(returnCode.getCode(), msg, dto);

        } catch (ValidationException e) {
            return new ResponseDto<ProjectDto>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ProjectDto>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<ProjectDto>(returnCode.getCode(), msg, null);
        }
    }
    
    /**
     * Retorna os projetos que atendem ao projeto informado como parametro.
     * 
     * @param id
     * @return ResponseDto<UsuarioDto>
     */
    @RequestMapping(path = RESTServices.REST_SEARCH, method = RequestMethod.POST)
    public ResponseDto<PageDto<ProjectDto>> search(@RequestBody PagedSearch<ProjectDto> search) {
        try {

            PageDto<ProjectDto> page = this.projectService.findByProject(search);

            ReturnCode returnCode = ReturnCode.SUCCESS;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            ResponseDto<PageDto<ProjectDto>> result = new ResponseDto<PageDto<ProjectDto>>(returnCode.getCode(), msg, page); 
            return result;

        } catch (ValidationException e) {
            return new ResponseDto<PageDto<ProjectDto>>(e.getCode(), e.getMessage(), null);
        } catch (EmptyResultDataAccessException e) {
            ReturnCode returnCode = ReturnCode.NOT_FOUND;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<ProjectDto>>(returnCode.getCode(), msg, null);
        } catch (Exception e) {
            ReturnCode returnCode = ReturnCode.FAIL;
            String msg = I18nUtils.getMsg(this.messageSource, returnCode.getMessage());
            return new ResponseDto<PageDto<ProjectDto>>(returnCode.getCode(), msg, null);
        }
    }
    
    /**
     * Recupera comboBox de projetos ativos
     * 
     * @return OperationReturn<PerfilDto>
     */
    @RequestMapping(path = RESTServices.REST_FIND_ACTIVES, method = RequestMethod.GET)
    public ResponseDto<List<ComboBoxDto>> getComboBoxActiveProjects() {
        try {
            List<ComboBoxDto> users = this.projectService.getComboBoxActiveProjects();
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
