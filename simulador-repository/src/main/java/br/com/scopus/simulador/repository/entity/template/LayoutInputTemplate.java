package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.LayoutInput;
import br.com.scopus.simulador.repository.entity.Transaction;
import br.com.scopus.simulador.repository.entity.enums.FieldType;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade LayoutInput.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class LayoutInputTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(LayoutInput.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("transaction", one(Transaction.class, NOVO_COM_ID));
                add("fieldType", FieldType.ALPHANUMERIC);
                add("name", name());
                add("size", random(Integer.class, range(1, 10)));
                add("ordinal", 1);
            }
        });
        Fixture.of(LayoutInput.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("transaction", one(Transaction.class, NOVO_COM_ID));
                add("fieldType", FieldType.ALPHANUMERIC);
                add("name", name());
                add("size", random(Integer.class, range(1, 10)));
                add("ordinal", 1);
            }
        });
    }
}