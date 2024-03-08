package br.com.scopus.simulador.portal;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.com.scopus.simulador.portal.auth.SecurityConfiguration;

@Configuration
@EnableWebMvc
public class JUnitSecurityTestContextLogin extends SecurityConfiguration {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.httpBasic();
    }

}
