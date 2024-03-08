package br.com.scopus.simulador.repository;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.template.TransactionTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio TransactionRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class TransactionRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(TransactionTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        Transaction transaction1 = Fixture.from(Transaction.class).gimme(TransactionTemplate.NOVO_COM_ID);
        Transaction transaction2 = new Transaction(transaction1.getPK());
        Transaction transaction3 = new Transaction(transaction1.getId(), transaction1.getTransactionType(),
            transaction1.getTransactionEncoding(), transaction1.getIdentification(), transaction1.getDescription(),
            transaction1.getTransactionParent(), transaction1.getRouterType(), transaction1.isParent(),
            transaction1.getLastAccess());
        Assert.assertEquals(transaction1, transaction2);
        Assert.assertEquals(transaction2, transaction3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {

        Transaction entity = Fixture.from(Transaction.class).gimme(TransactionTemplate.NOVO);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.transactionRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getIdentification(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        Transaction testEntity = this.transactionRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setIdentification(RandomStringUtils.randomAlphanumeric(8));
        entity = this.transactionRepository.save(entity);
        testEntity = this.transactionRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.transactionRepository.delete(entity);
        entity = this.transactionRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }

    @Test
    public void findAllOrderByIdentification() {

        List<Transaction> entities = Fixture.from(Transaction.class).gimme(10, TransactionTemplate.NOVO);
        this.transactionRepository.save(entities);

        List<Transaction> page = this.transactionRepository.findAllOrderByIdentification();

        Assert.assertNotNull(page);
    }
}
