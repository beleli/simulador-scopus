package br.com.scopus.simulador.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Classe padrao de retorno padrao para os servicos rest.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 * @param <T> tipo do retorno
 */
@AllArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "code" })
public class ResponseDto<T> {

    @Getter
    private Integer code;
    @Getter
    private String message;
    @Getter
    private T content; //Content genérico

}
