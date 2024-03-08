package br.com.scopus.simulador.dto.template;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.scopus.simulador.dto.UserDto;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias para o dto UserDto.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public class UserDtoTemplate implements TemplateLoaderDto {

    @Override
    public void load() {
        Fixture.of(UserDto.class).addTemplate(NOVO, new Rule() {
            {
                add("id", null);
                add("idProfile", random(Integer.class, range(1, 3)));
                add("name", name());
                add("email", RandomStringUtils.randomAlphanumeric(10) + "@scopus.com.br");
                add("enabled", true);
                add("changePassword", false);
                add("password", regex("\\w{10}"));
            }
        });
        Fixture.of(UserDto.class).addTemplate(NOVO_COM_ID, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("idProfile", random(Integer.class, range(1, 3)));
                add("name", name());
                add("email", RandomStringUtils.randomAlphanumeric(10) + "@scopus.com.br");
                add("enabled", true);
                add("changePassword", false);
                add("password", regex("\\w{10}"));
            }
        });
        Fixture.of(UserDto.class).addTemplate(INATIVO, new Rule() {
            {
                add("id", random(Long.class, range(1L, 500L)));
                add("idProfile", random(Integer.class, range(1, 3)));
                add("name", name());
                add("email", RandomStringUtils.randomAlphanumeric(10) + "@scopus.com.br");
                add("enabled", false);
                add("changePassword", false);
                add("password", regex("\\w{10}"));
            }
        });
    }

}
