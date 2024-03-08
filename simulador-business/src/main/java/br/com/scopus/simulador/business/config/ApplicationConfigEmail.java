package br.com.scopus.simulador.business.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Configuracao do envido de E-mail
 * 
 * @author Carlos Alberto Beleli Junior
 * @since 03/17
 * @version 1.0
 */
@NoArgsConstructor
@Component
public class ApplicationConfigEmail implements Serializable {

    private static final long serialVersionUID = 529370923293291064L;

    @Getter
    @Value("${email.sender.hostname}")
    private String hostname;

    @Getter
    @Value("${email.sender.password}")
    private String password;

    @Getter
    @Value("${email.sender.username}")
    private String username;

    @Getter
    @Value("${email.sender.port}")
    private Integer port;

    @Getter
    @Value("${email.sender.auth}")
    private String auth;

    @Getter
    @Value("${email.sender.protocol}")
    private String protocol;

    @Getter
    @Value("${email.sender.debug}")
    private Boolean debug;

    @Getter
    @Value("${email.sender.tlsenable}")
    private Boolean tlsEnable;

    @Getter
    @Value("${email.sender.from}")
    private String from;

    @Getter
    @Value("${email.assunto.nova.senha}")
    private String assuntoNovaSenha;

}
