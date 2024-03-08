package br.com.scopus.simulador.business.service.impl;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.scopus.simulador.business.JUnitBusinessTestContext;
import br.com.scopus.simulador.business.i18n.I18nKeys;
import br.com.scopus.simulador.business.service.UserService;
import br.com.scopus.simulador.dto.AlterarSenhaDto;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.UserDto;
import br.com.scopus.simulador.dto.template.UserDtoTemplate;
import br.com.scopus.simulador.repository.UserRepository;
import br.com.scopus.simulador.repository.entity.User;
import br.com.scopus.simulador.repository.entity.template.UserTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Testes unitario para a classe de servico UserService.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JUnitBusinessTestContext.class })
public class UserServiceImplTest extends JUnitBusinessTestContext {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(this.userRepository);
        FixtureFactoryLoader.loadTemplates(UserTemplate.class.getPackage().getName());
        FixtureFactoryLoader.loadTemplates(UserDtoTemplate.class.getPackage().getName());
    }

    @Test
    public void testCrud() throws Exception {

        /* customiza o comportamento dos mocks */
        Mockito.reset(this.userRepository);

        User user = Fixture.from(User.class).gimme(UserTemplate.NOVO_COM_ID);
        Mockito.when(this.userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(this.userRepository.findOne(Mockito.any(Long.class))).thenReturn(user);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos
         */
        UserDto userDto = Fixture.from(UserDto.class).gimme(UserDtoTemplate.NOVO);
        assertNotNull(userDto);
        assertNull(userDto.getId());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos
         */
        userDto = this.userService.insertDto(userDto);
        assertNotNull(userDto);
        assertNotNull(userDto.getId());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ser
         * implementado para o DTO)
         */
        UserDto testUser = this.userService.getDtoById(userDto.getId());
        assertEquals(testUser, userDto);

        /* altera o dto e verifica se a alteracao foi persistida */
        userDto = new UserDto(userDto.getId(), userDto.getIdProfile(), RandomStringUtils.randomAlphanumeric(10),
            userDto.getNameProfile(), userDto.getEmail(), userDto.getEnabled(), userDto.getChangePassword(),
            userDto.getPassword());
        userDto = this.userService.updateDto(userDto);
        testUser = this.userService.getDtoById(userDto.getId());
        assertEquals(testUser, userDto);

        try {

            /* remove o dto e verifica se ele foi removido */
            this.userService.deleteDtoById(userDto.getId());

            /* customiza o comportamento dos mocks */
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.NenhumRegistroEncontrado.getKey(),
                User.class.getSimpleName(), user.getId());
            Mockito.when(this.userRepository.findOne(Mockito.any(Long.class)))
                .thenThrow(new EmptyResultDataAccessException(msg, 1));

            userDto = this.userService.getDtoById(userDto.getId());
            Assert.fail();

        } catch (EmptyResultDataAccessException e) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.NenhumRegistroEncontrado.getKey(),
                User.class.getSimpleName(), testUser.getId());
            Assert.assertThat(e.getMessage(), CoreMatchers.containsString(msg));
        }

    }

    @Test
    public void findByEmail() {

        /* customiza o comportamento dos mocks */
        Mockito.reset(this.userRepository);

        User user = Fixture.from(User.class).gimme(UserTemplate.NOVO_COM_ID);
        Mockito.when(this.userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserDto userDto = this.userService.findByEmail(user.getEmail());
        Assert.assertNotNull(userDto);
        Assert.assertEquals(userDto.getId(), user.getId());
    }

    @Test
    public void changePassowrd() {
        User user = Fixture.from(User.class).gimme(UserTemplate.NOVO_COM_ID);
        Mockito.when(this.userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        AlterarSenhaDto alterarSenhaDto = new AlterarSenhaDto("teste@teste.com", user.getPassword(), "novaSenha");
        this.userService.alterarSenha(alterarSenhaDto);

        Mockito.verify(this.userRepository, Mockito.atLeast(1)).save(Mockito.any(User.class));

    }

    @Test
    public void changePassowrd_parametroInvalidoNull() {
        try {

            AlterarSenhaDto alterarSenhaDto = null;
            this.userService.alterarSenha(alterarSenhaDto);
            Assert.fail();

        } catch (ValidationException e) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            Assert.assertThat(e.getMessage(), CoreMatchers.containsString(msg));
        }
    }

    @Test
    public void changePassowrd_parametroInvalidoEmailNull() {
        try {
            AlterarSenhaDto alterarSenhaDto = new AlterarSenhaDto();

            this.userService.alterarSenha(alterarSenhaDto);
            Assert.fail();

        } catch (ValidationException e) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            Assert.assertThat(e.getMessage(), CoreMatchers.containsString(msg));
        }
    }

    @Test
    public void changePassowrd_usuarioNaoEncontrado() {
        try {
            User user = Fixture.from(User.class).gimme(UserTemplate.NOVO_COM_ID);
            Mockito.when(this.userRepository.findByEmail(user.getEmail())).thenReturn(user);

            AlterarSenhaDto alterarSenhaDto = new AlterarSenhaDto("login", "", "");

            this.userService.alterarSenha(alterarSenhaDto);
            Assert.fail();

        } catch (ValidationException e) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.UsuarioNaoEncontrado.getKey());
            Assert.assertThat(e.getMessage(), CoreMatchers.containsString(msg));
        }
    }

    @Test
    public void changePassowrd_usuarioSenhaInvalida() {
        try {
            User user = Fixture.from(User.class).gimme(UserTemplate.NOVO_COM_ID);
            Mockito.when(this.userRepository.findByEmail(user.getEmail())).thenReturn(user);

            AlterarSenhaDto alterarSenhaDto = new AlterarSenhaDto(user.getEmail(), user.getPassword().concat("xxx"),
                "");

            this.userService.alterarSenha(alterarSenhaDto);
            Assert.fail();

        } catch (ValidationException e) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.SenhaInvalida.getKey());
            Assert.assertThat(e.getMessage(), CoreMatchers.containsString(msg));
        }
    }

    @Test
    public void changePassowrd_usuarioInativo() {
        try {
            User user = Fixture.from(User.class).gimme(UserTemplate.NOVO_COM_ID);
            Mockito.when(this.userRepository.findByEmail(user.getEmail())).thenReturn(user);

            AlterarSenhaDto alterarSenhaDto = new AlterarSenhaDto(user.getEmail(), user.getPassword(), "");
            user.setEnabled(false);

            this.userService.alterarSenha(alterarSenhaDto);
            Assert.fail();

        } catch (ValidationException e) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.UsuarioInativo.getKey());
            Assert.assertThat(e.getMessage(), CoreMatchers.containsString(msg));
        }
    }

    @Test
    public void test_getComboBoxProfiles() {
        List<ComboBoxDto> dto = this.userService.getComboBoxProfiles();
        Assert.assertNotNull(dto);
    }
}
