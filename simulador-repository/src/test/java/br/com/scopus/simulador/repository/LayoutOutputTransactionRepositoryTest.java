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
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.template.LayoutOutputTransactionTemplate;
import br.com.scopus.simulador.repository.entity.template.TransactionTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio LayoutOutputTransactionRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class LayoutOutputTransactionRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient LayoutOutputTransactionRepository LayoutOutputTransactionRepository;

    @Autowired
    private transient TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(LayoutOutputTransactionTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        LayoutOutputTransaction entity1 = Fixture.from(LayoutOutputTransaction.class)
            .gimme(LayoutOutputTransactionTemplate.NOVO_COM_ID);
        LayoutOutputTransaction entity2 = new LayoutOutputTransaction(entity1.getPK());
        LayoutOutputTransaction entity3 = new LayoutOutputTransaction(entity1.getId(), entity1.getTransaction(),
            entity1.getDescription());
        Assert.assertEquals(entity1, entity2);
        Assert.assertEquals(entity2, entity3);
    }

    /**
     * Realiza um teste das funcionalidades CRUD... relevante para todas as entidades.
     */
    @Test
    public void testCrud() {

        Transaction transaction = Fixture.from(Transaction.class).gimme(TransactionTemplate.NOVO);
        transaction = this.transactionRepository.save(transaction);

        LayoutOutputTransaction entity = Fixture.from(LayoutOutputTransaction.class)
            .gimme(LayoutOutputTransactionTemplate.NOVO);
        entity.setTransaction(transaction);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.LayoutOutputTransactionRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getDescription(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        LayoutOutputTransaction testEntity = this.LayoutOutputTransactionRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setDescription(RandomStringUtils.randomAlphanumeric(8));
        entity = this.LayoutOutputTransactionRepository.save(entity);
        testEntity = this.LayoutOutputTransactionRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.LayoutOutputTransactionRepository.delete(entity);
        entity = this.LayoutOutputTransactionRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }
}
