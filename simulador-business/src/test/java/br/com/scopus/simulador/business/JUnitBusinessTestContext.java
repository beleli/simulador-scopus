package br.com.scopus.simulador.business;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.jerimum.fw.auth.MD5PasswordEncoder;
import br.com.jerimum.fw.config.JerimumEnvironment;
import br.com.jerimum.fw.logging.LoggerConfigurator;
import br.com.jerimum.fw.logging.LoggerUtils;
import br.com.scopus.simulador.business.config.ApplicationConfig;
import br.com.scopus.simulador.repository.ConfigurationRepository;
import br.com.scopus.simulador.repository.LayoutInputRepository;
import br.com.scopus.simulador.repository.LayoutOutputRepository;
import br.com.scopus.simulador.repository.LayoutOutputTransactionRepository;
import br.com.scopus.simulador.repository.LayoutVersionRepository;
import br.com.scopus.simulador.repository.ProfileRoleRepository;
import br.com.scopus.simulador.repository.ProjectRepository;
import br.com.scopus.simulador.repository.ProjectUserRepository;
import br.com.scopus.simulador.repository.TestMassInputRepository;
import br.com.scopus.simulador.repository.TestMassOutputRepository;
import br.com.scopus.simulador.repository.TestMassRepository;
import br.com.scopus.simulador.repository.TestScenarioRepository;
import br.com.scopus.simulador.repository.TransactionParentRepository;
import br.com.scopus.simulador.repository.TransactionRepository;
import br.com.scopus.simulador.repository.UserRepository;
import junit.framework.TestCase;

/**
 * Classe base para configuracao de testes da camada de negocio.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Configuration
@PropertySource({ "${spring.config.location}" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = { "${basePackages}.business", "${basePackages}.repository.entity.converter" })
@ContextConfiguration(classes = { ApplicationEmailContext.class })
public class JUnitBusinessTestContext extends TestCase implements InitializingBean {

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

        // System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("http.proxyHost", "iwacorp.scopus.com.br");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("https.proxyHost", "iwacorp.scopus.com.br");
        System.setProperty("https.proxyPort", "80");

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
    public PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

    @Bean
    public static LocalValidatorFactoryBean validatorFactory() {
        LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
        validatorFactory.setProviderClass(HibernateValidator.class);
        return validatorFactory;
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public ProfileRoleRepository profileRoleRepository() {
        return Mockito.mock(ProfileRoleRepository.class);
    }
    
    @Bean
    public TransactionRepository transactionRepository() {
        return Mockito.mock(TransactionRepository.class);
    }
    @Bean
    public LayoutInputRepository layoutInputRepository() {
        return Mockito.mock(LayoutInputRepository.class);
    }
    
    @Bean
    public LayoutOutputRepository layoutOutputRepository() {
        return Mockito.mock(LayoutOutputRepository.class);
    }
    
    @Bean
    public LayoutOutputTransactionRepository layoutOutputTransactionRepository() {
        return Mockito.mock(LayoutOutputTransactionRepository.class);
    }
    
    @Bean
    public LayoutVersionRepository layoutOutputVersionRepository() {
        return Mockito.mock(LayoutVersionRepository.class);
    }
    
    @Bean
    public TransactionParentRepository transactionParentRepository() {
        return Mockito.mock(TransactionParentRepository.class);
    }
    
    @Bean
    public ProjectRepository projectRepository() {
        return Mockito.mock(ProjectRepository.class);
    }
    
    @Bean
    public ProjectUserRepository projectUserRepository() {
        return Mockito.mock(ProjectUserRepository.class);
    }
    
    @Bean
    public ConfigurationRepository configurationRepository() {
        return Mockito.mock(ConfigurationRepository.class);
    }
    
    @Bean
    public TestScenarioRepository testScenarioRepository() {
        return Mockito.mock(TestScenarioRepository.class);
    }
    
    @Bean
    public TestMassRepository testMassRepository() {
        return Mockito.mock(TestMassRepository.class);
    }
    
    @Bean
    public TestMassInputRepository testMassInputRepository() {
        return Mockito.mock(TestMassInputRepository.class);
    }
    
    @Bean
    public TestMassOutputRepository testMassOutputRepository() {
        return Mockito.mock(TestMassOutputRepository.class);
    }
}
