package br.com.scopus.simulador.business;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import br.com.scopus.simulador.business.config.ApplicationConfigEmail;

@Configuration
public class ApplicationEmailContext {

    @Autowired
    private ApplicationConfigEmail appConfig;

    @Bean(name = "velocityEngine")
    public VelocityEngineFactoryBean velocityEngine() {

        VelocityEngineFactoryBean velocityEngine = new VelocityEngineFactoryBean();

        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("resource.loader", "class");
        velocityProperties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityProperties.setProperty("input.encoding", StandardCharsets.UTF_8.toString());
        velocityProperties.setProperty("output.encoding", StandardCharsets.UTF_8.toString());
        velocityEngine.setVelocityProperties(velocityProperties);
        Velocity.init(velocityProperties);
        return velocityEngine;
    }

    @Bean(name = "mailSender")
    public JavaMailSender mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.appConfig.getHostname());
        mailSender.setPort(this.appConfig.getPort());
        mailSender.setUsername(this.appConfig.getUsername());
        mailSender.setPassword(this.appConfig.getPassword());
        mailSender.setProtocol(this.appConfig.getProtocol());

        Properties javaMailProperties = criarJavaMailProperties(this.appConfig.getFrom(),
            this.appConfig.getProtocol(), this.appConfig.getAuth().toString(),
            this.appConfig.getTlsEnable().toString(),
            this.appConfig.getDebug().toString());
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    private Properties criarJavaMailProperties(String from, String protocol, String auth, String tlsEnable, String debug) {
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.from", from);
        javaMailProperties.setProperty("mail.transport.protocol", protocol);
        javaMailProperties.setProperty("mail.smtp.auth", auth);
        javaMailProperties.setProperty("mail.smtp.starttls.enable", tlsEnable);
        javaMailProperties.setProperty("mail.debug", debug);
        return javaMailProperties;
    }

}
