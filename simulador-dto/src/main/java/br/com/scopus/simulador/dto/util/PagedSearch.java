package br.com.scopus.simulador.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe parametro padrao de buscas paginadas
 * 
 * @author Jefferson Borges - jbsantos@scopus.com.br
 * @since 06/2016
 * @version 1.0
 * @param <T> dto da busca
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PagedSearch<T> {
    
    private Integer page;
    private Integer limit;
    private T item;
}