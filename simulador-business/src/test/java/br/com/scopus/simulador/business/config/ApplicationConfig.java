package br.com.scopus.simulador.business.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * Armazena as propriedades configuradas para a aplicacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Component
public class ApplicationConfig implements Serializable {

    private static final long serialVersionUID = 7896624401620243915L;

    @Getter
    @Value("${applicationName}")
    private String applicationName;

    @Getter
    @Value("${basePackages}")
    private String basePackages;

    @Getter
    @Value("${environmentName}")
    private String environmentName;

}

