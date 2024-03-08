package br.com.scopus.simulador.portalws.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Propriedades de configuracao da base de dados.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@NoArgsConstructor
@Component
public class ApplicationConfigDatabase implements Serializable {

    private static final long serialVersionUID = 7896624401620243915L;

    @Getter
    @Value("${database.hostname}")
    private String hostname;

    @Getter
    @Value("${database.username}")
    private String username;

    @Getter
    @Value("${database.password}")
    private String password;

    @Getter
    @Value("${database.port}")
    private String port;

    @Getter
    @Value("${database.databaseName}")
    private String databaseName;

    @Getter
    @Value("${database.owner}")
    private String owner;
    
    @Getter
    @Value("${database.url}")
    private String url;

    @Getter
    @Value("${database.driverClassName}")
    private String driverClassName;

    @Getter
    @Value("${database.databasePlatform}")
    private String databasePlatform;

    @Getter
    @Value("${database.show.sql}")
    private Boolean showSql;

    @Getter
    @Value("${database.jndi.datasource.name}")
    private String jndiDatasourceName;

}
