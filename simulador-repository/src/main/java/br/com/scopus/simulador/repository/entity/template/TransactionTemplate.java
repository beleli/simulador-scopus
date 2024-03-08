package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.enums.TransactionEncoding;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade Transaction.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class TransactionTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(Transaction.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("transactionType", TransactionType.CICS);
                add("transactionEncoding", TransactionEncoding.EBCDIC);
                add("identification", regex("\\w{4}"));
                add("description", regex("\\w{30}"));
                add("transactionParent", null);
                add("routerType", null);
                add("parent", false);
                add("lastAccess", null);
            }
        });
        Fixture.of(Transaction.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("transactionType", TransactionType.CICS);
                add("transactionEncoding", TransactionEncoding.EBCDIC);
                add("identification", regex("\\w{4}"));
                add("description", regex("\\w{30}"));
                add("transactionParent", null);
                add("routerType", null);
                add("parent", false);
                add("lastAccess", null);
            }
        });
    }

}