package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.TestScenario;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade Transaction.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class TestScenarioTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(TestScenario.class).addTemplate(NOVO, new Rule() {
            {

                add("id", null);
                add("description", regex("\\w{60}"));
                add("transaction", one(Transaction.class, NOVO_COM_ID));
                
               
            }
        });
        Fixture.of(TestScenario.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("description", regex("\\w{60}"));
                add("transaction", one(Transaction.class, NOVO_COM_ID));
            }
        });
    }

}