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
import br.com.scopus.simulador.repository.entity.LayoutVersion;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.template.LayoutInputTemplate;
import br.com.scopus.simulador.repository.entity.template.LayoutOutputTransactionTemplate;
import br.com.scopus.simulador.repository.entity.template.LayoutVersionTemplate;
import br.com.scopus.simulador.repository.entity.template.TransactionTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio layoutVersionRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class LayoutVersionRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient LayoutVersionRepository layoutVersionRepository;

    @Autowired
    private transient TransactionRepository transactionRepository;
    
    @Autowired
    private transient LayoutInputRepository layoutInputRepository;
    
    @Autowired
    private transient LayoutOutputTransactionRepository layoutOutputTransactionRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(LayoutVersionTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        LayoutVersion entity1 = Fixture.from(LayoutVersion.class).gimme(LayoutVersionTemplate.NOVO_COM_ID);
        LayoutVersion entity2 = new LayoutVersion(entity1.getPK());
        LayoutVersion entity3 = new LayoutVersion(entity1.getId(), entity1.getLayoutInput(),
            entity1.getLayoutOutputTransaction(), entity1.getValue());
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

        LayoutInput layoutInput = Fixture.from(LayoutInput.class).gimme(LayoutInputTemplate.NOVO);
        layoutInput.setTransaction(transaction);
        layoutInput = this.layoutInputRepository.save(layoutInput);

        LayoutOutputTransaction layoutOutputTransaction = Fixture.from(LayoutOutputTransaction.class)
            .gimme(LayoutOutputTransactionTemplate.NOVO);
        layoutOutputTransaction.setTransaction(transaction);
        layoutOutputTransaction = this.layoutOutputTransactionRepository.save(layoutOutputTransaction);

        LayoutVersion entity = Fixture.from(LayoutVersion.class).gimme(LayoutVersionTemplate.NOVO);
        entity.setLayoutInput(layoutInput);
        entity.setLayoutOutputTransaction(layoutOutputTransaction);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.layoutVersionRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getLayoutInput().getName(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        LayoutVersion testEntity = this.layoutVersionRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setValue(RandomStringUtils.randomAlphanumeric(8));
        entity = this.layoutVersionRepository.save(entity);
        testEntity = this.layoutVersionRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.layoutVersionRepository.delete(entity);
        entity = this.layoutVersionRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }
}
