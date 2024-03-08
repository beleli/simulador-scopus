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
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.template.LayoutInputTemplate;
import br.com.scopus.simulador.repository.entity.template.TransactionTemplate;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

/**
 * Teste unitario para o repositorio LayoutInputRepository.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestContext.class)
public class LayoutInputRepositoryTest extends RepositoryTestContext {

    @Autowired
    private transient LayoutInputRepository layoutInputRepository;
    
    @Autowired
    private transient TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates(LayoutInputTemplate.class.getPackage().getName());
    }

    @Test
    public void testEquals() {

        LayoutInput entity1 = Fixture.from(LayoutInput.class).gimme(LayoutInputTemplate.NOVO_COM_ID);
        LayoutInput entity2 = new LayoutInput(entity1.getPK());
        LayoutInput entity3 = new LayoutInput(entity1.getId(), entity1.getTransaction(), entity1.getFieldType(),
            entity1.getName(), entity1.getSize(), entity1.getOrdinal());
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
        
        LayoutInput entity = Fixture.from(LayoutInput.class).gimme(LayoutInputTemplate.NOVO);
        entity.setTransaction(transaction);

        /*
         * verifica se os campos autogerados (autoincremento por exemplo) sao nulos.
         */
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.getPK());

        /*
         * insere a entidade e verifica se os campos autogerados nao sao nulos.
         */
        entity = this.layoutInputRepository.save(entity);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getPK());
        Assert.assertEquals(entity.getName(), entity.getLabel());

        /*
         * verifica se o registro consultado tem o valor esperado (metodo equals precisa ter sido
         * implementado para a entidade).
         */
        LayoutInput testEntity = this.layoutInputRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * altera a entidade e verifica se a alteracao foi persistida.
         */
        entity.setName(RandomStringUtils.randomAlphanumeric(8));
        entity = this.layoutInputRepository.save(entity);
        testEntity = this.layoutInputRepository.findOne(entity.getPK());
        Assert.assertEquals(testEntity, entity);

        /*
         * remove a entidade e verifica se ela foi removida.
         */
        this.layoutInputRepository.delete(entity);
        entity = this.layoutInputRepository.findOne(entity.getPK());
        Assert.assertNull(entity);
    }
}
