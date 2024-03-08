package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.ProjectUser;
import br.com.scopus.simulador.repository.entity.User;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade ProjectUser.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public class ProjectUserTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(ProjectUser.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("project", one(Project.class, NOVO_COM_ID));
                add("user", one(User.class, NOVO_COM_ID));
            }
        });
        Fixture.of(ProjectUser.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("project", one(Project.class, NOVO_COM_ID));
                add("user", one(User.class, NOVO_COM_ID));
            }
        });
    }

}
