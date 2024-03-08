package br.com.scopus.simulador.business.service.impl;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.scopus.simulador.business.JUnitBusinessTestContext;
import br.com.scopus.simulador.business.service.EmailService;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JUnitBusinessTestContext.class })
public class EmailServiceImplTest extends JUnitBusinessTestContext {

    @Mock
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

    @Before
    public void setUp() {
        /* inicia os mocks */
        MockitoAnnotations.initMocks(this);

        /* injeta as classes que nao sao instancias de mock (@Autowired) */
        ReflectionTestUtils.setField(this.emailService, "mailSender", this.mailSender);
    }

    @Test
    public void testSendMail() throws InterruptedException {

        String password = UUID.randomUUID().toString().substring(0, 10);
        this.emailService.enviarEmailRecuperarSenha(password, "cabjunior@scopus.com.br");

        Thread.sleep(2000);

        Mockito.verify(this.mailSender, Mockito.atLeast(1)).send(Mockito.any(MimeMessagePreparator.class));

    }

    @Test
    public void testSendMail_exception() throws Exception {

        Mockito.doThrow(new RuntimeException()).when(this.mailSender).send(Mockito.any(MimeMessagePreparator.class));

        String password = UUID.randomUUID().toString().substring(0, 10);
        this.emailService.enviarEmailRecuperarSenha(password, "cabjunior@scopus.com.br");
        
        Thread.sleep(2000);

        Mockito.verify(this.mailSender, Mockito.atLeast(1)).send(Mockito.any(MimeMessagePreparator.class));

    }

}
