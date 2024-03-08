package br.com.scopus.simulador.repository.entity.template;

import java.util.Date;
import br.com.scopus.simulador.repository.entity.Project;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade Project.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class ProjectTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(Project.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("code", regex("\\w{32}"));
                add("description", regex("\\w{100}"));
                add("beginDate", new Date());
                add("endDate", new Date());
            }
        });
        Fixture.of(Project.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("code", regex("\\w{32}"));
                add("description", regex("\\w{100}"));
                add("beginDate", new Date());
                add("endDate", new Date());
            }
        });
    }

}
