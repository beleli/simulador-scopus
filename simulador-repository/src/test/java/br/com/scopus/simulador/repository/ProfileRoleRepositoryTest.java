package br.com.scopus.simulador.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.scopus.simulador.repository.entity.ProfileRole;
import br.com.scopus.simulador.repository.entity.Role;
import br.com.scopus.simulador.repository.entity.enums.Profile;
import br.com.scopus.simulador.repository.entity.template.ProfileRoleTemplate;
import br.com.scopus.simulador.repository.entity.template.RoleTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio ProfileRoleRepository.
 * 
 * @author Jefferson Borges - jbantos@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class ProfileRoleRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient RoleRepository roleRepository;
    @Autowired
    private transient ProfileRoleRepository profileRoleRepository;
    
    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(ProfileRoleTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        ProfileRole profileRole1 = Fixture.from(ProfileRole.class).gimme(ProfileRoleTemplate.NOVO_COM_ID);
        ProfileRole profileRole2 = new ProfileRole(profileRole1.getPK());
        ProfileRole profileRole3 = new ProfileRole(profileRole1.getId(), profileRole1.getProfile(),
            profileRole1.getRole());
        Assert.assertEquals(profileRole1, profileRole2);
        Assert.assertEquals(profileRole2, profileRole3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {

        Role role = Fixture.from(Role.class).gimme(RoleTemplate.NOVO);
        role.setId(1000L);
        role = this.roleRepository.save(role);

        ProfileRole profileRole = Fixture.from(ProfileRole.class).gimme(ProfileRoleTemplate.NOVO);
        profileRole.setRole(role);
        
        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(profileRole);
        Assert.assertNull(profileRole.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        profileRole = this.profileRoleRepository.save(profileRole);
        Assert.assertNotNull(profileRole);
        Assert.assertNotNull(profileRole.getPK());
        Assert.assertNotNull(profileRole.getLabel());
        Assert.assertEquals(profileRole.getProfile(), Profile.USUARIO);
        Assert.assertEquals(profileRole.getRole(), role);

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        ProfileRole testProfileRole = this.profileRoleRepository.findOne(profileRole.getPK());
        Assert.assertEquals(testProfileRole, profileRole);
        Assert.assertEquals(profileRole.getProfile(), testProfileRole.getProfile());
        Assert.assertEquals(profileRole.getRole(), testProfileRole.getRole());

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        profileRole.setProfile(Profile.ADMINISTRADOR);
        profileRole = this.profileRoleRepository.save(profileRole);
        testProfileRole = this.profileRoleRepository.findOne(profileRole.getPK());
        Assert.assertEquals(testProfileRole, profileRole);
        Assert.assertEquals(profileRole.getProfile(), testProfileRole.getProfile());
        Assert.assertEquals(profileRole.getRole(), testProfileRole.getRole());

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.profileRoleRepository.delete(profileRole);
        profileRole = this.profileRoleRepository.findOne(profileRole.getPK());
        Assert.assertNull(profileRole);
    }

}
