package br.com.scopus.simulador.business.service;

import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.service.CrudService;
import br.com.scopus.simulador.dto.ConfigurationDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.entity.Configuration;

/**
 * Interface de servico para transações.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public interface ConfigurationService  extends CrudService<ConfigurationDto, Configuration>{

    /***
     * Sava a configuração.
     * 
     * @param dto
     * @return dto
     * @throws ValidationException
     * @throws ServiceException
     */
    ConfigurationDto save(ConfigurationDto dto) throws ValidationException, ServiceException;

    /***
     * Busca a configuração de acordo com  nome.
     * 
     * @param search
     * @return
     */
    PageDto<ConfigurationDto> search(PagedSearch<ConfigurationDto> search);
}
