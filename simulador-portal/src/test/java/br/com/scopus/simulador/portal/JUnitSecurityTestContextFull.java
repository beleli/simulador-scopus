package br.com.scopus.simulador.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.com.scopus.simulador.portal.auth.ApplicationUserDetailsService;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class JUnitSecurityTestContextFull extends WebSecurityConfigurerAdapter {

    public static final String DUMMY_USER = "email@scopus.com.br";
    public static final String DUMMY_PASSWORD = "123456";

    @Autowired
    private ApplicationUserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/**").hasRole("ADMIN").anyRequest().anonymous().and()
            .httpBasic();
    }
}
