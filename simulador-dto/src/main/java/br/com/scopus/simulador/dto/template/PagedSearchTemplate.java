package br.com.scopus.simulador.dto.template;

import br.com.scopus.simulador.dto.ProjectDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * Template para a criacao de instancias para o dto ApplicationDto.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 */
public class PagedSearchTemplate implements TemplateLoaderDto {
    
    public static final String PROJECT = "PROJECT";

    @Override
    public void load() {
        Fixture.of(PagedSearch.class).addTemplate(PROJECT, new Rule() {
            {
                add("page", random(Integer.class, range(1L, 3L)));
                add("limit", 5);
                add("item", one(ProjectDto.class, NOVO_COM_ID));
            }
        });
    }
}