package br.com.scopus.simulador.portal.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;

import br.com.jerimum.fw.auth.JerimumDefaultSecurityConfiguration;
import br.com.jerimum.fw.auth.MD5PasswordEncoder;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.portal.util.Constants;

/**
 * Configuracoes de seguranca para a aplicacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends JerimumDefaultSecurityConfiguration {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ApplicationUserDetailsService userDetailsService;

    @Autowired
    @Override
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) throws IOException, ServletException {
                if (StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), request.getHeader("X-Login-Ajax-call"))) {
                    String msg = I18nUtils.getMsg(messageSource, ReturnCode.SUCCESS.getMessage());
                    String retorno = jsonRetorno(ReturnCode.SUCCESS, msg);
                    printResponse(response, HttpServletResponse.SC_OK, retorno);
                } else {
                    new SavedRequestAwareAuthenticationSuccessHandler().onAuthenticationSuccess(request, response,
                        authentication);
                }
            }
        };
    }

    @Override
    protected AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException exception) throws IOException, ServletException {
                String retorno = jsonRetorno(ReturnCode.ACCESS_DENIED, exception.getMessage());
                printResponse(response, HttpServletResponse.SC_UNAUTHORIZED, retorno);
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        Map<String, String[]> authorities = getAuthorities();
        if (MapUtils.isNotEmpty(authorities)) {
            for (String url : authorities.keySet()) {
                http.authorizeRequests().antMatchers(url).hasAnyAuthority(authorities.get(url));
            }
        }

        http.authorizeRequests().antMatchers(getUnsecuredResources()).permitAll().anyRequest().authenticated().and()
            .formLogin().defaultSuccessUrl(getDefaultLoggedPage()).loginProcessingUrl("/login")
            .usernameParameter("username").passwordParameter("password")
            .successHandler(ajaxAuthenticationSuccessHandler()).failureHandler(authenticationFailureHandler())
            .loginPage(getLoginPage()).permitAll().and().logout().logoutUrl("/logout").logoutSuccessUrl(getLoginPage())
            .permitAll().and().csrf().csrfTokenRepository(csrfTokenRepository()).and()
            .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class).exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler());
    }

    protected AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                AccessDeniedException accessDeniedException) throws IOException, ServletException {
                UsuarioLogado usr = (UsuarioLogado) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
                String msg = I18nUtils.getMsg(messageSource, ReturnCode.ACCESS_DENIED.getMessage());
                String retorno = jsonRetorno(ReturnCode.ACCESS_DENIED, msg);
                if (usr != null) {
                    printResponse(response, HttpServletResponse.SC_FORBIDDEN, retorno);
                } else {
                    printResponse(response, HttpServletResponse.SC_UNAUTHORIZED, retorno);
                }
            }
        };
    }

    /**
     * Retorna um json baseado nos parametros informados.
     * 
     * @param codigoRetorno
     * @param mensagem
     * @return String
     */
    private String jsonRetorno(ReturnCode codigoRetorno, String mensagem) {
        return String.format("{\"codigoRetorno\":\"%s\",\"mensagemRetorno\":\"%s\"}", codigoRetorno, mensagem);
    }

    /**
     * Escreve o retorno do processo de login.
     * 
     * @param response
     * @param status
     * @param text
     * @throws IOException
     */
    private void printResponse(HttpServletResponse response, int status, String text) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);
        response.setCharacterEncoding("ISO-8859-1");
        response.getWriter().print(text);
        response.getWriter().flush();
    }

    @Override
    protected String getLoginPage() {
        return Constants.PAGINA_LOGIN;
    }

    @Override
    protected String getDefaultLoggedPage() {
        return Constants.PAGINA_HOME;
    }

    @Override
    protected String getAccessDeniedPage() {
        return null;// Constantes.PAGINA_ACESSO_NEGADO;
    }

    @Override
    protected String[] getUnsecuredResources() {
        return new String[] { "/", "/index.html", "/pages/*.html", "/fonts/**", "/resources/**", "/wro/**",
            "/pages/open/**", "/template/**", "/rest/login/logout", "/rest/login/recuperarSenha/**" };
    }

    @Override
    protected Map<String, String[]> getAuthorities() {

        Map<String, String[]> authorities = new HashMap<String, String[]>();

        /*
         * TODO: Montar um map com os recursos (urls) e as roles (deve possuir o prefixo 'ROLE_')
         * que dao acesso a esse recurso.
         * 
         * POR EXEMPLO:
         */
        authorities.put("/pages/restricted/home/home.html", new String[] { "ROLE_VIEW_HOME" });
        authorities.put("/pages/restricted/change-password/change-password.html", new String[] { "ROLE_VIEW_CHANGE_PASSWORD" });
        authorities.put("/pages/restricted/users/users.html", new String[] { "ROLE_VIEW_USER" });
        authorities.put("/pages/restricted/projects/projects.html", new String[] { "ROLE_VIEW_PROJECT" });
        authorities.put("/pages/restricted/transactions/transactions.html", new String[] { "ROLE_VIEW_TRANSACTION" });
        authorities.put("/pages/restricted/test-scenario/test-scenario.html", new String[] { "ROLE_VIEW_TEST_SCENARIO" });
        authorities.put("/pages/restricted/test-mass/test-mass.html", new String[] { "ROLE_VIEW_TEST_MASS" });
        
        return authorities;
    }

    @Override
    protected PasswordEncoder getPasswordEncoder() {
        return new MD5PasswordEncoder();
    }

}
