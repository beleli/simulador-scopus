package br.com.scopus.simulador.portalws.rest;

import javax.servlet.Filter;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.scopus.simulador.business.service.UserService;
import br.com.scopus.simulador.dto.UserDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.template.UserDtoTemplate;
import br.com.scopus.simulador.portalws.JUnitSecurityTestContextFull;
import br.com.scopus.simulador.portalws.JUnitTestContext;
import br.com.scopus.simulador.portalws.TestUtil;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o servico rest UserController.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JUnitTestContext.class, JUnitSecurityTestContextFull.class })
@WebAppConfiguration
public class UserControllerTest extends JUnitTestContext {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).addFilters(this.springSecurityFilterChain).build();
        FixtureFactoryLoader.loadTemplates(UserDtoTemplate.class.getPackage().getName());
    }

    @Test
    public void save() throws Exception {

        final UserDto requestDto = Fixture.from(UserDto.class).gimme(UserDtoTemplate.NOVO);
        ReturnCode success = ReturnCode.SUCCESS;
        String msg = I18nUtils.getMsg(this.messageSource, success.getMessage());

        /* customiza o comportamento do mock */
        Mockito.reset(this.userService);
        UserDto responseDto = Fixture.from(UserDto.class).gimme(UserDtoTemplate.NOVO_COM_ID);
        Mockito.when(this.userService.salvarUsuario(requestDto)).thenReturn(responseDto);

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(RESTServices.CONTROLLER_USER + RESTServices.REST_SAVE)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(JUnitSecurityTestContextFull.DUMMY_USER,
                    JUnitSecurityTestContextFull.DUMMY_PASSWORD))
                .accept(MediaType.APPLICATION_JSON).contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.anything()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.equalTo(success.getCode())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.equalTo(msg))).andExpect(
                MockMvcResultMatchers.jsonPath("$.content.id", CoreMatchers.equalTo(responseDto.getId().intValue())));
    }

    @Test
    public void save_ValidationException() throws Exception {

        final UserDto requestDto = Fixture.from(UserDto.class).gimme(UserDtoTemplate.NOVO);

        /* customiza o comportamento do mock */
        Mockito.reset(this.userService);
        ReturnCode result = ReturnCode.INVALID_PARAMETERS;
        String msg = I18nUtils.getMsg(this.messageSource, result.getMessage());
        Mockito.when(this.userService.salvarUsuario(requestDto)).thenThrow(new ValidationException(result.getCode(), msg));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(RESTServices.CONTROLLER_USER + RESTServices.REST_SAVE)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(JUnitSecurityTestContextFull.DUMMY_USER,
                    JUnitSecurityTestContextFull.DUMMY_PASSWORD))
                .accept(MediaType.APPLICATION_JSON).contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.anything()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.equalTo(result.getCode())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.equalTo(msg)));
    }

    @Test
    public void save_Exception() throws Exception {

        final UserDto requestDto = Fixture.from(UserDto.class).gimme(UserDtoTemplate.NOVO);

        /* customiza o comportamento do mock */
        Mockito.reset(this.userService);
        ReturnCode result = ReturnCode.FAIL;
        String msg = I18nUtils.getMsg(this.messageSource, result.getMessage());
        Mockito.when(this.userService.salvarUsuario(requestDto)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(RESTServices.CONTROLLER_USER + RESTServices.REST_SAVE)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(JUnitSecurityTestContextFull.DUMMY_USER,
                    JUnitSecurityTestContextFull.DUMMY_PASSWORD))
                .accept(MediaType.APPLICATION_JSON).contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.anything()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.equalTo(result.getCode())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.equalTo(msg)));
    }

    @Test
    public void findById() throws Exception {

        Long idUser = 1L;

        /* customiza o comportamento do mock */
        UserDto dto = Fixture.from(UserDto.class).gimme(UserDtoTemplate.NOVO_COM_ID);
        Mockito.when(this.userService.getDtoById(idUser)).thenReturn(dto);

        ReturnCode success = ReturnCode.SUCCESS;
        String msg = I18nUtils.getMsg(this.messageSource, success.getMessage());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(RESTServices.CONTROLLER_USER + RESTServices.REST_FIND_BY_ID, idUser)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(JUnitSecurityTestContextFull.DUMMY_USER,
                    JUnitSecurityTestContextFull.DUMMY_PASSWORD))
                .accept(MediaType.APPLICATION_JSON).contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.anything()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.equalTo(success.getCode())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.equalTo(msg)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.id", CoreMatchers.equalTo(dto.getId().intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.name", CoreMatchers.equalTo(dto.getName())));
    }

    @Test
    public void findByEmail() throws Exception {

        String email = "email@scopus.com.br";

        /* customiza o comportamento do mock */
        UserDto dto = Fixture.from(UserDto.class).gimme(UserDtoTemplate.NOVO_COM_ID);
        Mockito.when(this.userService.findByEmail(email)).thenReturn(dto);

        ReturnCode success = ReturnCode.SUCCESS;
        String msg = I18nUtils.getMsg(this.messageSource, success.getMessage());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(RESTServices.CONTROLLER_USER + RESTServices.REST_FIND_BY_EMAIL, email)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(JUnitSecurityTestContextFull.DUMMY_USER,
                    JUnitSecurityTestContextFull.DUMMY_PASSWORD))
                .accept(MediaType.APPLICATION_JSON).contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.anything()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.equalTo(success.getCode())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.equalTo(msg)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.id", CoreMatchers.equalTo(dto.getId().intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.name", CoreMatchers.equalTo(dto.getName())));
    }
 }
