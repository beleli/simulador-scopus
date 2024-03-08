package br.com.scopus.simulador.dto.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Classe de metodos uteis para paginacao
 * 
 * @author Jefferson Borges - jbsantos@scopus.com.br
 * @since 05/2016
 * @version 1.0
 */
public final class PageUtil {

    private PageUtil() {
        super();
        throw new IllegalStateException();
    }

    /**
     * Create PageRequest based on MensagemDTO
     * 
     * @param {@link PagedSearch}
     * @return {@link PageRequest} - return null if page and size equals 0
     */
    public static <T> PageRequest createPageRequest(PagedSearch<T> pagedSearch) {
        return createPageRequest(pagedSearch.getPage(), pagedSearch.getLimit(), null);
    }

    /**
     * Create PageRequest
     * 
     * @param page
     * @param size
     * @return {@link PageRequest} - return null if page and size equals 0
     */
    public static PageRequest createPageRequest(int page, int size) {
        return createPageRequest(page, size, null);
    }

    /**
     * Create PageRequest based on MensagemDTO
     * 
     * @param {@link PagedSearch}
     * @param {@link Sort}
     * @return {@link PageRequest} - return null if page and size equals 0
     */
    public static <T> PageRequest createPageRequest(PagedSearch<T> pagedSearch, Sort sort) {
        return createPageRequest(pagedSearch.getPage(), pagedSearch.getLimit(), sort);
    }

    /**
     * Create PageRequest
     * 
     * @param page
     * @param size
     * @param {@link Sort}
     * @return {@link PageRequest} - return null if page and size equals 0
     */
    public static PageRequest createPageRequest(int page, int size, Sort sort) {
        page = (page == 0 ? page : page - 1);
        if (page == 0 && size == 0) {
            return null;
        } else {
            return new PageRequest(page, size, sort);
        }
    }

}
