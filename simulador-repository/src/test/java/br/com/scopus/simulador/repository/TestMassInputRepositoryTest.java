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

import br.com.scopus.simulador.repository.entity.LayoutInput;
import br.com.scopus.simulador.repository.entity.LayoutOutputTransaction;
import br.com.scopus.simulador.repository.entity.TestMass;
import br.com.scopus.simulador.repository.entity.TestMassInput;
import br.com.scopus.simulador.repository.entity.TestScenario;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.template.LayoutInputTemplate;
import br.com.scopus.simulador.repository.entity.template.LayoutOutputTransactionTemplate;
import br.com.scopus.simulador.repository.entity.template.TestMassInputTemplate;
import br.com.scopus.simulador.repository.entity.template.TestMassTemplate;
import br.com.scopus.simulador.repository.entity.template.TestScenarioTemplate;
import br.com.scopus.simulador.repository.entity.template.TransactionTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio TestMassInputRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class TestMassInputRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient TestMassInputRepository testMassInputRepository;

    @Autowired
    private transient TestMassRepository testMassRepository;

    @Autowired
    private transient TransactionRepository transactionRepository;

    @Autowired
    private transient TestScenarioRepository testScenarioRepository;
    
    @Autowired
    private transient LayoutInputRepository layoutInputRepository;

    @Autowired
    private transient LayoutOutputTransactionRepository layoutOutputTransactionRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(TestMassInputTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        TestMassInput testMassInput1 = Fixture.from(TestMassInput.class).gimme(TestMassInputTemplate.NOVO_COM_ID);
        TestMassInput testMassInput2 = new TestMassInput(testMassInput1.getPK());
        TestMassInput testMassInput3 = new TestMassInput(testMassInput1.getId(), testMassInput1.getTestMass(),
            testMassInput1.getLayoutInput(), testMassInput1.getValue());
        Assert.assertEquals(testMassInput1, testMassInput2);
        Assert.assertEquals(testMassInput2, testMassInput3);
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
        
        LayoutInput layoutInput = Fixture.from(LayoutInput.class).gimme(LayoutInputTemplate.NOVO);
        layoutInput.setTransaction(transaction);
        layoutInputRepository.save(layoutInput);
        
        TestMassInput entity = Fixture.from(TestMassInput.class).gimme(TestMassInputTemplate.NOVO);
        entity.setTestMass(testMass);
        entity.setLayoutInput(layoutInput);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.testMassInputRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getValue(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        TestMassInput testEntity = this.testMassInputRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setValue(RandomStringUtils.randomAlphanumeric(50));
        entity = this.testMassInputRepository.save(entity);
        testEntity = this.testMassInputRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.testMassInputRepository.delete(entity);
        entity = this.testMassInputRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }

}
