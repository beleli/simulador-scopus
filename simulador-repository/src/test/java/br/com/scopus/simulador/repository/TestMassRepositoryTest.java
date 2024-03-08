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

import br.com.scopus.simulador.repository.entity.LayoutOutputTransaction;
import br.com.scopus.simulador.repository.entity.TestMass;
import br.com.scopus.simulador.repository.entity.TestScenario;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.template.LayoutOutputTransactionTemplate;
import br.com.scopus.simulador.repository.entity.template.TestMassTemplate;
import br.com.scopus.simulador.repository.entity.template.TestScenarioTemplate;
import br.com.scopus.simulador.repository.entity.template.TransactionTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio testMassRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class TestMassRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient TestMassRepository testMassRepository;

    @Autowired
    private transient TransactionRepository transactionRepository;

    @Autowired
    private transient TestScenarioRepository testScenarioRepository;

    @Autowired
    private transient LayoutOutputTransactionRepository layoutOutputTransactionRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(TestMassTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        TestMass testMass1 = Fixture.from(TestMass.class).gimme(TestMassTemplate.NOVO_COM_ID);
        TestMass testMass2 = new TestMass(testMass1.getPK());
        TestMass testMass3 = new TestMass(testMass1.getId(), testMass1.getTestScenario(),
            testMass1.getLayoutOutputTransaction(), testMass1.getProject(), testMass1.getDescription(),
            testMass1.getErrorAverage(), testMass1.getTimeout(), testMass1.getOccurrences(), testMass1.getRestart(),
            testMass1.getReturnCode());
        Assert.assertEquals(testMass1, testMass2);
        Assert.assertEquals(testMass2, testMass3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {

        Transaction transaction = Fixture.from(Transaction.class).gimme(TransactionTemplate.NOVO);
        transaction = this.transactionRepository.save(transaction);

        TestScenario testScenario = Fixture.from(TestScenario.class).gimme(TestScenarioTemplate.NOVO);
        testScenario.setTransaction(transaction);
        testScenario = this.testScenarioRepository.save(testScenario);

        LayoutOutputTransaction layoutOutputTransaction = Fixture.from(LayoutOutputTransaction.class)
            .gimme(LayoutOutputTransactionTemplate.NOVO);
        layoutOutputTransaction.setTransaction(transaction);
        layoutOutputTransaction = this.layoutOutputTransactionRepository.save(layoutOutputTransaction);

        TestMass entity = Fixture.from(TestMass.class).gimme(TestMassTemplate.NOVO);
        entity.setTestScenario(testScenario);
        entity.setLayoutOutputTransaction(layoutOutputTransaction);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.testMassRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getDescription(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        TestMass testEntity = this.testMassRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setDescription(RandomStringUtils.randomAlphanumeric(50));
        entity = this.testMassRepository.save(entity);
        testEntity = this.testMassRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.testMassRepository.delete(entity);
        entity = this.testMassRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }

}
