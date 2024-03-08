package br.com.scopus.simulador.repository;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.scopus.simulador.repository.entity.Role;
import br.com.scopus.simulador.repository.entity.template.RoleTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio RoleRepository.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class RoleRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient RoleRepository roleRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(RoleTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        Role role1 = Fixture.from(Role.class).gimme(RoleTemplate.NOVO_COM_ID);
        Role role2 = new Role(role1.getPK());
        Role role3 = new Role(role1.getId(), role1.getName());
        Assert.assertEquals(role1, role2);
        Assert.assertEquals(role2, role3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    // @Test
    public void testCrud() {

        Role role = Fixture.from(Role.class).gimme(RoleTemplate.NOVO);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(role);
        Assert.assertNull(role.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        role = this.roleRepository.save(role);
        Assert.assertNotNull(role);
        Assert.assertNotNull(role.getPK());
        Assert.assertEquals(role.getName(), role.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        Role testRole = this.roleRepository.findOne(role.getPK());
        Assert.assertEquals(testRole, role);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        role.setName(RandomStringUtils.randomAlphanumeric(10));
        role = this.roleRepository.save(role);
        testRole = this.roleRepository.findOne(role.getPK());
        Assert.assertEquals(testRole, role);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.roleRepository.delete(role);
        role = this.roleRepository.findOne(role.getPK());
        Assert.assertNull(role);

    }

}
