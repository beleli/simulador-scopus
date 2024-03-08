package br.com.scopus.simulador.portal;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import br.com.jerimum.fw.auth.MD5PasswordEncoder;
import br.com.jerimum.fw.config.JerimumEnvironment;
import br.com.jerimum.fw.logging.LoggerConfigurator;
import br.com.jerimum.fw.logging.LoggerUtils;
import br.com.scopus.simulador.portal.auth.ApplicationUserDetailsService;
import br.com.scopus.simulador.portal.config.ApplicationConfig;
import junit.framework.TestCase;

/**
 * Classe base para configuracao de testes da camada de controlladores (servicos rest).
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Configuration
@ComponentScan(basePackages = { "${basePackages}.portal.aop", "${basePackages}.portal.config", "${basePackages}.portal.rest" })
@ContextConfiguration(classes = { JUnitTestContext.class })
@DirtiesContext
@PropertySource({ "${spring.config.location}" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class JUnitTestContext extends TestCase implements InitializingBean {

    public static String DEFAULT_TEST_ENVIRONMENT = "test";

    {
        String jerimumEnvironmentPath = "classpath:META-INF/environment/" + DEFAULT_TEST_ENVIRONMENT
            + "/application.properties";
        String logbackConfigurationFile = new File(System.getProperty("user.dir")
            + "/src/main/resources/META-INF/environment/" + DEFAULT_TEST_ENVIRONMENT + "/logback.xml")
                .getAbsolutePath();
        String jerimumConfigurationFile = StringUtils.replace(logbackConfigurationFile, "logback.xml",
            "application.properties");
        String jerimumEnvironmentName = JerimumEnvironment.getEnvironment();
        System.setProperty("spring.config.location", jerimumEnvironmentPath);
        System.setProperty("logback.configurationFile", logbackConfigurationFile);
        System.setProperty("jerimum.configurationFile", jerimumConfigurationFile);
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, jerimumEnvironmentName);
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, jerimumEnvironmentName);

        new LoggerConfigurator(logbackConfigurationFile).configure();

        LoggerUtils.logInfo(this.getClass(), "================================================================");
        LoggerUtils.logInfo(this.getClass(), ".:  Starting Jerimum Application :.");
        LoggerUtils.logInfo(this.getClass(), ".:  Environment: {} :.", jerimumEnvironmentName);
        LoggerUtils.logInfo(this.getClass(), ".:  Jerimum configuration file: {} :.", jerimumConfigurationFile);
        LoggerUtils.logInfo(this.getClass(), ".:  Logback configuration file: {} :.", logbackConfigurationFile);
    }

    @Autowired
    private transient ApplicationConfig appConfig;
    @Autowired
    private transient Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length == 0) {
            LoggerUtils.logInfo(this.getClass(), "Active Spring Profiles: None");
        } else {
            LoggerUtils.logInfo(this.getClass(), "Active Spring Profiles: {}", (Object[]) profiles);
        }
        LoggerUtils.logInfo(this.getClass(), "Active Maven Profile: {}", this.appConfig.getEnvironmentName());
        LoggerUtils.logInfo(this.getClass(), "Application name: {}", this.appConfig.getApplicationName());
        LoggerUtils.logInfo(this.getClass(), "Base package: {}", this.appConfig.getBasePackages());
    }

    @Bean
    @Autowired
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(Environment environment) {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setEnvironment(environment);
        return configurer;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
    }

    @Bean
    public ApplicationUserDetailsService userDetailsService() {
        return new ApplicationUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }
    
}
