package br.com.scopus.simulador.repository;

import java.io.File;
import java.util.Collection;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.jerimum.fw.logging.LoggerConfigurator;
import br.com.jerimum.fw.logging.LoggerUtils;
import br.com.scopus.simulador.repository.config.ApplicationConfig;
import junit.framework.TestCase;

/**
 * Contexto de teste para os repositorios.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Configuration
@PropertySource({ "${spring.config.location}" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = { "${basePackages}" })
@EnableJpaRepositories
public abstract class RepositoryTestContext extends TestCase implements InitializingBean {

    public static String DEFAULT_TEST_ENVIRONMENT = "test";
    {
        String jerimumEnvironmentPath = "classpath:META-INF/environment/" + DEFAULT_TEST_ENVIRONMENT
            + "/application.properties";
        String logbackConfigurationFile = getClass()
            .getResource("/META-INF/environment/" + DEFAULT_TEST_ENVIRONMENT + "/logback.xml").getFile();
        String jerimumConfigurationFile = StringUtils.replace(logbackConfigurationFile, "logback.xml",
            "application.properties");

        System.setProperty("spring.config.location", jerimumEnvironmentPath);
        System.setProperty("logback.configurationFile", logbackConfigurationFile);
        System.setProperty("jerimum.configurationFile", jerimumConfigurationFile);
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, DEFAULT_TEST_ENVIRONMENT);
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, DEFAULT_TEST_ENVIRONMENT);

        new LoggerConfigurator(logbackConfigurationFile).configure();

        LoggerUtils.logInfo(this.getClass(), "================================================================");
        LoggerUtils.logInfo(this.getClass(), ".:  Starting Jerimum Application :.");
        LoggerUtils.logInfo(this.getClass(), ".:  Environment: {} :.", DEFAULT_TEST_ENVIRONMENT);
        LoggerUtils.logInfo(this.getClass(), ".:  Jerimum configuration file: {} :.", jerimumConfigurationFile);
        LoggerUtils.logInfo(this.getClass(), ".:  Logback configuration file: {} :.", logbackConfigurationFile);
    }

    @Autowired
    private transient Environment environment;

    @Autowired
    private transient ApplicationConfig appConfig;

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
    public static LocalValidatorFactoryBean validatorFactory() {
        LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
        validatorFactory.setProviderClass(HibernateValidator.class);
        return validatorFactory;
    }

    @Bean
    public DataSource datasource() {

        File diretorioScripts = new File("./src/main/resources/db/migration");
        Collection<File> listaArquivosScript = FileUtils.listFiles(diretorioScripts, null, false);
        String[] scripts = new String[listaArquivosScript.size()];
        int cont = 0;
        for (File script : listaArquivosScript) {
            scripts[cont++] = String.format("db/migration/%s", script.getName());
        }

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).addScripts(scripts).build();
        return db;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory);
        return manager;
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(this.appConfig.getAppConfigDatabase().getShowSql());
        adapter.setDatabasePlatform(appConfig.getAppConfigDatabase().getDatabasePlatform());

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan(this.appConfig.getBasePackages());
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setJpaVendorAdapter(adapter);
        return factory;
    }

}
