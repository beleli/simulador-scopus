package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.LayoutInput;
import br.com.scopus.simulador.repository.entity.TestMass;
import br.com.scopus.simulador.repository.entity.TestMassInput;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade Transaction.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class TestMassInputTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(TestMassInput.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("testMass", one(TestMass.class, NOVO_COM_ID));
                add("layoutInput", one(LayoutInput.class, NOVO_COM_ID));
                add("value", regex("\\w{60}"));
            }
        });
        Fixture.of(TestMassInput.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("testMass", one(TestMass.class, NOVO_COM_ID));
                add("layoutInput", one(LayoutInput.class, NOVO_COM_ID));
                add("value", regex("\\w{60}"));
            }
        });
    }
}
