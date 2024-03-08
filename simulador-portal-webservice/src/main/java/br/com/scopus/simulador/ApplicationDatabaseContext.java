package br.com.scopus.simulador;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import br.com.scopus.simulador.portalws.config.ApplicationConfig;
import br.com.scopus.simulador.portalws.config.ApplicationConfigDatabase;

@Configuration
public class ApplicationDatabaseContext {

    @Autowired
    private ApplicationConfig appConfig;

    @Bean
    public DataSource datasource() {
        ApplicationConfigDatabase dbProperties = this.appConfig.getAppConfigDatabase();

        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        DataSource dataSource = dsLookup.getDataSource(dbProperties.getJndiDatasourceName());
        return dataSource;
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
        factory.setJpaProperties(configureJpaProperties());
        return factory;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * Configura as propriedades jpa do datasource.
     * 
     * @return Properties
     */
    private Properties configureJpaProperties() {
        final ApplicationConfigDatabase dbConfig = this.appConfig.getAppConfigDatabase();
        final Properties jpaProperties = new Properties();
        if (StringUtils.isNotBlank(dbConfig.getOwner())) {
            jpaProperties.put(org.hibernate.cfg.Environment.DEFAULT_SCHEMA, dbConfig.getOwner());
        }
        jpaProperties.put(org.hibernate.cfg.Environment.SHOW_SQL, dbConfig.getShowSql());
        return jpaProperties;
    }

}
