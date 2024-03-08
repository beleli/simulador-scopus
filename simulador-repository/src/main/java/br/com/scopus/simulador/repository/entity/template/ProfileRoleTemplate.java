package br.com.scopus.simulador.repository.entity.template;

import br.com.scopus.simulador.repository.entity.ProfileRole;
import br.com.scopus.simulador.repository.entity.Role;
import br.com.scopus.simulador.repository.entity.enums.Profile;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias da entidade Profile.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public class ProfileRoleTemplate implements TemplateLoaderEntity {

    @Override
    public void load() {
        Fixture.of(ProfileRole.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("profile", Profile.USUARIO);
                add("role", one(Role.class, NOVO_COM_ID));
            }
        });
        Fixture.of(ProfileRole.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 3L)));
                add("profile", Profile.USUARIO);
                add("role", one(Role.class, NOVO_COM_ID));
            }
        });
    }

}
