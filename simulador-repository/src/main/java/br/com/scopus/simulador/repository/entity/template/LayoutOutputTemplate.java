package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.LayoutOutput;
import br.com.scopus.simulador.repository.entity.LayoutOutputTransaction;
import br.com.scopus.simulador.repository.entity.enums.FieldType;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade LayoutOutput.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class LayoutOutputTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(LayoutOutput.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("layoutOutputTransaction", one(LayoutOutputTransaction.class, NOVO_COM_ID));
                add("fieldType", FieldType.ALPHANUMERIC);
                add("name", name());
                add("size", random(Integer.class, range(1, 10)));
                add("ordinal", 1);
            }
        });
        Fixture.of(LayoutOutput.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("layoutOutputTransaction", one(LayoutOutputTransaction.class, NOVO_COM_ID));
                add("fieldType", FieldType.ALPHANUMERIC);
                add("name", name());
                add("size", random(Integer.class, range(1, 10)));
                add("ordinal", 1);
            }
        });
    }
}