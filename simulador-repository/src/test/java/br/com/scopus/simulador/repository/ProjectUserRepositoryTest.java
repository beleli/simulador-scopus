package br.com.scopus.simulador.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.ProjectUser;
import br.com.scopus.simulador.repository.entity.User;
import br.com.scopus.simulador.repository.entity.template.ProjectTemplate;
import br.com.scopus.simulador.repository.entity.template.ProjectUserTemplate;
import br.com.scopus.simulador.repository.entity.template.UserTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio projectUserRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class ProjectUserRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient ProjectRepository projectRepository;
    @Autowired
    private transient UserRepository userRepository;
    @Autowired
    private transient ProjectUserRepository projectUserRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(ProjectUserTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        ProjectUser entity1 = Fixture.from(ProjectUser.class).gimme(ProjectUserTemplate.NOVO_COM_ID);
        ProjectUser entity2 = new ProjectUser(entity1.getPK());
        ProjectUser entity3 = new ProjectUser(entity1.getId(), entity1.getProject(), entity1.getUser());
        Assert.assertEquals(entity1, entity2);
        Assert.assertEquals(entity2, entity3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {

        Project project = Fixture.from(Project.class).gimme(ProjectTemplate.NOVO);
        project = this.projectRepository.save(project);

        User user = Fixture.from(User.class).gimme(UserTemplate.NOVO);
        user = this.userRepository.save(user);

        ProjectUser entity = Fixture.from(ProjectUser.class).gimme(ProjectUserTemplate.NOVO);
        entity.setProject(project);
        entity.setUser(user);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.projectUserRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertNotNull(entity.getLabel());
        Assert.assertEquals(entity.getProject(), project);
        Assert.assertEquals(entity.getUser(), user);

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        ProjectUser entityTest = this.projectUserRepository.findOne(entity.getPK());
        Assert.assertEquals(entityTest, entity);
        Assert.assertEquals(entity.getProject(), entityTest.getProject());
        Assert.assertEquals(entity.getUser(), entityTest.getUser());

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        User newUser = Fixture.from(User.class).gimme(UserTemplate.NOVO);
        /* TODO: validar a geração do e-mail*/
        newUser.setEmail("a" + newUser.getEmail());
        newUser = this.userRepository.save(newUser);
        entity.setUser(newUser);
        entity = this.projectUserRepository.save(entity);

        entityTest = this.projectUserRepository.findOne(entity.getPK());
        Assert.assertEquals(entityTest, entity);
        Assert.assertEquals(entity.getProject(), entityTest.getProject());
        Assert.assertEquals(entity.getUser(), entityTest.getUser());

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.projectUserRepository.delete(entity);
        entity = this.projectUserRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }

}
