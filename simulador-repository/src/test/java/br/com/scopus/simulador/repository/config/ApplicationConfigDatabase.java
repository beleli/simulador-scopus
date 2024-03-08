package br.com.scopus.simulador.repository.config;

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
    @Value("${database.databasePlatform}")
    private String databasePlatform;

    @Getter
    @Value("${database.show.sql}")
    private Boolean showSql;

}
