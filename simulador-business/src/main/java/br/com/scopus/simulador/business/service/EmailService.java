package br.com.scopus.simulador.business.service;

import java.util.Map;
import br.com.scopus.simulador.dto.EmailDto;

/**
 * Interface de servico para emails.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public interface EmailService {
	
    /**
     * Envio de e-mail para recuperação de senha
     * 
     * @param novaSenha
     * @param emailDestinatario
     * 
     */
    boolean enviarEmailRecuperarSenha(String novaSenha, String emailDestinatario);
    
    /**
     * Envio de e-mail para cadastro de usuario
     * 
     * @param novaSenha
     * @param emailDestinatario
     * 
     */
    boolean enviarEmailCadastro(String novaSenha, String emailDestinatario);

    /**
     * Envio de e-mail assincrono
     * 
     * @param templatePath
     * @param email
     * @param model
     */
    void sendAsyncEmail(String templatePath, EmailDto email, Map<String, Object> model);

    /**
     * Envio de e-mail assincrono
     * 
     * @param templatePath
     * @param email
     * @param model
     */
    boolean sendSyncEmail(String templatePath, EmailDto email, Map<String, Object> model);

}
