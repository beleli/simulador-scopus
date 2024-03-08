package br.com.scopus.simulador.business.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import br.com.jerimum.fw.logging.LoggerUtils;
import br.com.scopus.simulador.business.config.ApplicationConfigEmail;
import br.com.scopus.simulador.business.service.EmailService;
import br.com.scopus.simulador.dto.EmailDto;

/**
 * Implementação do envio de E-mail
 * 
 * @author Lesley A Diniz
 * @since 05/16
 * @version 1.0
 *
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    @Qualifier("mailSender")
    private JavaMailSender mailSender;

    @Autowired
    @Qualifier("velocityEngine")
    private VelocityEngine velocityEngine;

    @Autowired
    private ApplicationConfigEmail appConfigEmail;

    @Override
    public boolean enviarEmailRecuperarSenha(String novaSenha, String emailDestinatario) {
        Map<String, Object> model = new HashMap<>();
        model.put("novaSenha", novaSenha);

        EmailDto email = new EmailDto();
        email.getDestinatarios().add(emailDestinatario);
        email.setAssunto(appConfigEmail.getAssuntoNovaSenha());
        email.setRemetente(appConfigEmail.getFrom());

        return sendSyncEmail("template/emailRecuperarSenha.html.vm", email, model);
    }

    @Override
    public void sendAsyncEmail(final String templatePath, final EmailDto email, final Map<String, Object> model) {
        new Thread() {
            @Override
            public void run() {
                EmailServiceImpl.this.enviarEmail(templatePath, email, model);
            }
        }.start();
    }

    @Override
    public boolean sendSyncEmail(final String templatePath, final EmailDto email, final Map<String, Object> model) {
        return EmailServiceImpl.this.enviarEmail(templatePath, email, model);
    }

    /**
     * Realiza o envio do email de acordo como o objeto passado como parametro. Retorna
     * <code>true</code> caso o envio tenha sido realizado com sucesso.
     * 
     * @param email
     * @return boolean
     */
    private boolean enviarEmail(String templatePath, final EmailDto email, Map<String, Object> model) {

        try {

            /*
             * gera o corpo do email a partir de um template velocity (um arquivo html dentro do
             * pacote de resources 'template/emailRecuperarSenha.html.vm' por exemplo)
             */
            final String corpoEmail = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath,
                StandardCharsets.UTF_8.toString(), model);
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {

                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.toString());
                    message.setBcc(email.getDestinatarios().toArray(new String[] {}));
                    message.setFrom(email.getRemetente());
                    message.setSubject(email.getAssunto());
                    message.setText(corpoEmail, true);
                }
            };

            this.mailSender.send(preparator);

            return true;

        } catch (Exception e) {
            LoggerUtils.logError(this.getClass(), "Falha ao enviar e-mail.", e);
            return false;
        }

    }

    @Override
    public boolean enviarEmailCadastro(String novaSenha, String emailDestinatario) {
        Map<String, Object> model = new HashMap<>();
        model.put("novaSenha", novaSenha);

        EmailDto email = new EmailDto();
        email.getDestinatarios().add(emailDestinatario);
        email.setAssunto(appConfigEmail.getAssuntoNovaSenha());
        email.setRemetente(appConfigEmail.getFrom());

        return sendSyncEmail("template/emailCadastro.html.vm", email, model);
        
    }
}
