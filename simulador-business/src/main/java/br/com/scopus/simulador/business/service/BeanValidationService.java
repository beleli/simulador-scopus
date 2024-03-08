package br.com.scopus.simulador.business.service;

import br.com.jerimum.fw.entity.AbstractEntity;
import br.com.jerimum.fw.exception.ValidationException;

/**
 * Realiza a validação de entidade
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 03/2017
 * @version 1.0
 *
 */
public interface BeanValidationService {
    
    /**
     * valida a entidade
     * @param entity
     * @throws ValidationException
     */
    public <ENTITY extends AbstractEntity<?>> void validate(ENTITY entity);
}
