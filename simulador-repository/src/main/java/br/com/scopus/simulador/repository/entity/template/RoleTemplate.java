package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.Role;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade Role.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class RoleTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(Role.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("name", name());
            }
        });
        Fixture.of(Role.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 4L)));
                add("name", name());
            }
        });
    }

}
