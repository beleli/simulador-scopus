package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.Configuration;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade Configuration.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class ConfigurationTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(Configuration.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("transactionType", TransactionType.CICS);
                add("name", regex("\\w{30}"));
                add("port", random(Integer.class, range(1, 500)));
                add("timeout", random(Integer.class, range(1, 500)));
                add("bytesAccess", random(Integer.class, range(1, 500)));
            }
        });
        Fixture.of(Configuration.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(10L, 500L)));
                add("transactionType", TransactionType.CICS);
                add("name", regex("\\w{30}"));
                add("port", random(Integer.class, range(1, 500)));
                add("timeout", random(Integer.class, range(1, 500)));
                add("bytesAccess", random(Integer.class, range(1, 500)));
            }
        });
    }
}