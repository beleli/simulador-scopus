package br.com.scopus.simulador.repository.entity.template;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.scopus.simulador.repository.entity.User;
import br.com.scopus.simulador.repository.entity.enums.Profile;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade User.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public class UserTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(User.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("profile", Profile.ADMINISTRADOR);
                add("name", name());
                add("email", RandomStringUtils.randomAlphanumeric(10) + "@scopus.com.br");
                add("enabled", true);
                add("changePassword", false);
                add("password", regex("\\w{10}"));
            }
        });
        Fixture.of(User.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("profile", Profile.ADMINISTRADOR);
                add("name", name());
                add("email", RandomStringUtils.randomAlphanumeric(10) + "@scopus.com.br");
                add("enabled", true);
                add("changePassword", false);
                add("password", regex("\\w{10}"));
            }
        });
    }

}
