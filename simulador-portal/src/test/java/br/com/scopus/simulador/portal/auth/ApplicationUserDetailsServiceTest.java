package br.com.scopus.simulador.portal.auth;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import br.com.scopus.simulador.dto.template.UserDtoTemplate;
import br.com.scopus.simulador.portal.JUnitSecurityTestContextLogin;
import br.com.scopus.simulador.portal.JUnitTestContext;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para UserDetailsService.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JUnitTestContext.class, JUnitSecurityTestContextLogin.class })
@WebAppConfiguration
public class ApplicationUserDetailsServiceTest extends JUnitTestContext {

    private static final String DUMMY_USER = "admn@admn.com";
    private static final String DUMMY_PASSWORD = "123";
    private static final String LOGIN_URL = "/login";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).addFilters(this.springSecurityFilterChain).build();
        FixtureFactoryLoader.loadTemplates(UserDtoTemplate.class.getPackage().getName());

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserByUsername() throws Exception {

        Mockito.reset(this.restTemplate);
        String result = "{\"code\":0,\"message\":\"Serviço executado com sucesso\",\"content\":{\"id\":1,\"idProfile\":1,\"name\":\"Administrador\",\"email\":\"adm@adm.com\",\"enabled\":true,\"changePassword\":false,\"password\":\"202cb962ac59075b964b07152d234b70\"}}";
        String resultRoles = "{\"code\":0,\"message\":\"Serviço executado com sucesso\",\"content\":{\"listRole\":[\"ROLE_VIEW_HOME\",\"ROLE_VIEW_USER\",\"ROLE_VIEW_CHANGE_PASSWORD\"]}}";
        Mockito
            .when(this.restTemplate.getForObject(Mockito.any(String.class), Mockito.any(), Mockito.any(String.class)))
            .thenReturn(result, resultRoles); 

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LOGIN_URL)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(DUMMY_USER, DUMMY_PASSWORD)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
