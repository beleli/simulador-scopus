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

import br.com.scopus.simulador.repository.entity.Configuration;
import br.com.scopus.simulador.repository.entity.template.ConfigurationTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio ConfigurationRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class ConfigurationRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient ConfigurationRepository serviceRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(ConfigurationTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        Configuration service1 = Fixture.from(Configuration.class).gimme(ConfigurationTemplate.NOVO_COM_ID);
        Configuration service2 = new Configuration(service1.getPK());
        Configuration service3 = new Configuration(service1.getId(), service1.getTransactionType(), service1.getName(),
            service1.getPort(), service1.getTimeout(), service1.getBytesAccess());
        Assert.assertEquals(service1, service2);
        Assert.assertEquals(service2, service3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {

        Configuration entity = Fixture.from(Configuration.class).gimme(ConfigurationTemplate.NOVO);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.serviceRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getName(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        Configuration testEntity = this.serviceRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setName(RandomStringUtils.randomAlphanumeric(40));
        entity = this.serviceRepository.save(entity);
        testEntity = this.serviceRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.serviceRepository.delete(entity);
        entity = this.serviceRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }
}
