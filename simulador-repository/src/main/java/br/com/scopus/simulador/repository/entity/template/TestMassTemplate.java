package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.LayoutOutputTransaction;
import br.com.scopus.simulador.repository.entity.TestMass;
import br.com.scopus.simulador.repository.entity.TestScenario;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade Transaction.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class TestMassTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(TestMass.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("testScenario", one(TestScenario.class, NOVO_COM_ID));
                add("layoutOutputTransaction", one(LayoutOutputTransaction.class, NOVO_COM_ID));
                add("project", null);
                add("description", regex("\\w{60}"));
                add("errorAverage", random(Integer.class, range(0L, 100L)));
                add("timeout", random(Integer.class, range(90L, 1500L)));
                add("occurrences", random(Integer.class, range(1L, 10L)));
                add("restart", null);
                add("returnCode", random(Integer.class, range(1L, 10L)));
            }
        });
        Fixture.of(TestMass.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("testScenario", one(TestScenario.class, NOVO_COM_ID));
                add("layoutOutputTransaction", one(LayoutOutputTransaction.class, NOVO_COM_ID));
                add("project", null);
                add("description", regex("\\w{60}"));
                add("errorAverage", random(Integer.class, range(0L, 100L)));
                add("timeout", random(Integer.class, range(90L, 1500L)));
                add("occurrences", random(Integer.class, range(1L, 10L)));
                add("restart", null);
                add("returnCode", random(Integer.class, range(1L, 10L)));
            }
        });
    }

}