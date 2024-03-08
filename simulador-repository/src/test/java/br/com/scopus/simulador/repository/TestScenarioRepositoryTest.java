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

import br.com.scopus.simulador.repository.entity.TestScenario;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.template.TestScenarioTemplate;
import br.com.scopus.simulador.repository.entity.template.TransactionTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio TestScenarioRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class TestScenarioRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient TestScenarioRepository testScenarioRepository;
    
    @Autowired
    private transient TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(TestScenarioTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        TestScenario testScenario1 = Fixture.from(TestScenario.class).gimme(TestScenarioTemplate.NOVO_COM_ID);
        TestScenario testScenario2 = new TestScenario(testScenario1.getPK());
        TestScenario testScenario3 = new TestScenario(testScenario1.getId(), testScenario1.getDescription(), testScenario1.getTransaction());
        Assert.assertEquals(testScenario1, testScenario2);
        Assert.assertEquals(testScenario2, testScenario3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {
        
        Transaction transaction = Fixture.from(Transaction.class).gimme(TransactionTemplate.NOVO);
        transaction = this.transactionRepository.save(transaction);

        TestScenario entity = Fixture.from(TestScenario.class).gimme(TestScenarioTemplate.NOVO);
        entity.setTransaction(transaction);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.testScenarioRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getDescription(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        TestScenario testEntity = this.testScenarioRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setDescription(RandomStringUtils.randomAlphanumeric(50));
        entity = this.testScenarioRepository.save(entity);
        testEntity = this.testScenarioRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.testScenarioRepository.delete(entity);
        entity = this.testScenarioRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }

}
