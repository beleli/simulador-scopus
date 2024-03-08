package br.com.scopus.simulador.dto.template;

import br.com.six2six.fixturefactory.loader.TemplateLoader;

/**
 * Interface padrao para as classes de template.
 * 
 * @see <a href="https://github.com/six2six/fixture-factory">Fixture Factory Framework</a>
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public interface TemplateLoaderDto extends TemplateLoader {

    public static final String NOVO = "NOVO";
    public static final String NOVO_COM_ID = "NOVO_COM_ID";
    public static final String INATIVO = "INATIVO";

}
