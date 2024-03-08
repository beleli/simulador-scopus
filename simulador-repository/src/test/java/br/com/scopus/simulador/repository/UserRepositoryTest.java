package br.com.scopus.simulador.repository;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.scopus.simulador.repository.entity.User;
import br.com.scopus.simulador.repository.entity.enums.Profile;
import br.com.scopus.simulador.repository.entity.template.UserTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio UserRepository.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class UserRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;
    private Validator validator;

    @Before
    public void setUp() {
        this.validator = this.validatorFactoryBean.getValidator();
        FixtureFactoryLoader.loadTemplates(UserTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        User user1 = Fixture.from(User.class).gimme(UserTemplate.NOVO_COM_ID);
        User user2 = new User(user1.getPK(), null, user1.getLabel(), null, null, null, null);
        User user3 = new User(user1.getId(), null, user1.getName(), null, null, null, null);
        User user4 = new User(user1.getId(), null, null, null, null, null, null);
        User user5 = new User(user1.getId(), user1.getProfile(), user1.getName(), user1.getEmail(), user1.getEnabled(),
            user1.getChangePassword(), user1.getPassword());
        User user6 = new User();
        User user7 = new User(user1.getId() + 1, null, null, null, null, null, null);

        Assert.assertEquals(user1.getPK(), user1.getId());
        Assert.assertEquals(user1.getLabel(), user1.getName());
        Assert.assertEquals(user1, user2);
        Assert.assertEquals(user1, user3);
        Assert.assertEquals(user1, user4);
        Assert.assertEquals(user1, user5);
        Assert.assertNotEquals(user1, user6);
        Assert.assertNotEquals(user1, user7);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {

        User entity = Fixture.from(User.class).gimme(UserTemplate.NOVO);
        entity.setProfile(Profile.ADMINISTRADOR);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /* insere a entidade */
        entity = this.userRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getPK(), entity.getId());
        Assert.assertEquals(entity.getLabel(), entity.getName());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        User test = this.userRepository.findOne(entity.getPK());
        Assert.assertEquals(test, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setName(RandomStringUtils.randomAlphanumeric(10));
        entity = this.userRepository.save(entity);
        test = this.userRepository.findOne(entity.getPK());
        Assert.assertEquals(test, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.userRepository.delete(entity);
        entity = this.userRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }

    @Test
    public void testValidacaoCampos() {

        User entity = Fixture.from(User.class).gimme(UserTemplate.NOVO);
        Profile profile = entity.getProfile();

        /*
         * todos os atributos preenchidos corretamente
         */
        Set<ConstraintViolation<User>> constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(0, constraintViolations.size());

        /*
         * atributo obrigatorio 'profile' nao preenchido
         */
        entity.setProfile(null);
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("profile", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setProfile(profile);

        /*
         * atributo obrigatorio 'name' nao preenchido
         */
        entity.setName(null);
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("name", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setName(RandomStringUtils.randomAlphanumeric(10));

        /*
         * atributo 'name' com tamanho maximo excedido
         */
        entity.setName(RandomStringUtils.randomAlphanumeric(61));
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("name", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setName(RandomStringUtils.randomAlphanumeric(10));

        /*
         * atributo obrigatorio 'email' nao preenchido
         */
        entity.setEmail(null);
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("email", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setEmail("dinodasilvasauro@scopus.com.br");

        /*
         * atributo 'email' com tamanho maximo excedido
         */
        entity.setEmail(String.format("%s@scopus.com.br", RandomStringUtils.randomAlphanumeric(250)));
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals("email", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setEmail("dinodasilvasauro@scopus.com.br");

        /*
         * atributo 'email' com formato invalido
         */
        entity.setEmail("abcdefg");
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("email", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setEmail("dinodasilvasauro@scopus.com.br");

        /*
         * atributo obrigatorio 'enabled' nao preenchido
         */
        entity.setEnabled(null);
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("enabled", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setEnabled(true);

        /*
         * atributo obrigatorio 'changePassword' nao preenchido
         */
        entity.setChangePassword(null);
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("changePassword", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setChangePassword(true);

        /*
         * atributo obrigatorio 'password' nao preenchido
         */
        entity.setPassword(null);
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("password", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setPassword(RandomStringUtils.randomAlphanumeric(8));

        /*
         * atributo 'password' com tamanho maximo excedido
         */
        entity.setPassword(RandomStringUtils.randomAlphanumeric(256));
        constraintViolations = this.validator.validate(entity);
        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("password", constraintViolations.iterator().next().getPropertyPath().toString());
        entity.setPassword(RandomStringUtils.randomAlphanumeric(8));

    }

}
