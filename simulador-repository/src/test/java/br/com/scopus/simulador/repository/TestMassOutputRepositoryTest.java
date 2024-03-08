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

import br.com.scopus.simulador.repository.entity.LayoutOutput;
import br.com.scopus.simulador.repository.entity.LayoutOutputTransaction;
import br.com.scopus.simulador.repository.entity.TestMass;
import br.com.scopus.simulador.repository.entity.TestMassOutput;
import br.com.scopus.simulador.repository.entity.TestScenario;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.template.LayoutOutputTemplate;
import br.com.scopus.simulador.repository.entity.template.LayoutOutputTransactionTemplate;
import br.com.scopus.simulador.repository.entity.template.TestMassOutputTemplate;
import br.com.scopus.simulador.repository.entity.template.TestMassTemplate;
import br.com.scopus.simulador.repository.entity.template.TestScenarioTemplate;
import br.com.scopus.simulador.repository.entity.template.TransactionTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio testMassOutputRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class TestMassOutputRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient TestMassOutputRepository testMassOutputRepository;

    @Autowired
    private transient TestMassRepository testMassRepository;

    @Autowired
    private transient TransactionRepository transactionRepository;

    @Autowired
    private transient TestScenarioRepository testScenarioRepository;
    
    @Autowired
    private transient LayoutOutputRepository layoutOutputRepository;

    @Autowired
    private transient LayoutOutputTransactionRepository layoutOutputTransactionRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(TestMassOutputTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        TestMassOutput testMassOutput1 = Fixture.from(TestMassOutput.class).gimme(TestMassOutputTemplate.NOVO_COM_ID);
        TestMassOutput testMassOutput2 = new TestMassOutput(testMassOutput1.getPK());
        TestMassOutput testMassOutput3 = new TestMassOutput(testMassOutput1.getId(), testMassOutput1.getTestMass(),
            testMassOutput1.getLayoutOutput(), testMassOutput1.getLayoutInput(), testMassOutput1.getValue());
        Assert.assertEquals(testMassOutput1, testMassOutput2);
        Assert.assertEquals(testMassOutput2, testMassOutput3);
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

        TestMass testMass = Fixture.from(TestMass.class).gimme(TestMassTemplate.NOVO);
        testMass.setTestScenario(testScenario);
        testMass.setLayoutOutputTransaction(layoutOutputTransaction);
        testMassRepository.save(testMass);
        
        LayoutOutput layoutOutput = Fixture.from(LayoutOutput.class).gimme(LayoutOutputTemplate.NOVO);
        layoutOutput.setLayoutOutputTransaction(layoutOutputTransaction); 
        layoutOutput = this.layoutOutputRepository.save(layoutOutput);
        
        TestMassOutput entity = Fixture.from(TestMassOutput.class).gimme(TestMassOutputTemplate.NOVO);
        entity.setTestMass(testMass);
        entity.setLayoutOutput(layoutOutput);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.testMassOutputRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getValue(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        TestMassOutput testEntity = this.testMassOutputRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setValue(RandomStringUtils.randomAlphanumeric(50));
        entity = this.testMassOutputRepository.save(entity);
        testEntity = this.testMassOutputRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.testMassOutputRepository.delete(entity);
        entity = this.testMassOutputRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }

}
