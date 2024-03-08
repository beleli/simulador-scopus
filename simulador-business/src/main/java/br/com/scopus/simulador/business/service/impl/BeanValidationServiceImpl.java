package br.com.scopus.simulador.business.service.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.jerimum.fw.constants.ReturnCode;
import br.com.jerimum.fw.entity.AbstractEntity;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.scopus.simulador.business.service.BeanValidationService;

/**
 * Realiza a validação de entidade
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 03/2017
 * @version 1.0
 *
 */
@Service
public class BeanValidationServiceImpl implements BeanValidationService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;
    
    private Validator validator;
    
    @Override
    public <ENTITY extends AbstractEntity<?>> void validate(ENTITY entity) {
        Set<ConstraintViolation<ENTITY>> constraintViolations = getValidator().validate(entity);
        for (ConstraintViolation<ENTITY> constraintViolation : constraintViolations) {
            String msg = I18nUtils.getMsg(this.messageSource, constraintViolation.getMessage());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }
    }
    
    private Validator getValidator(){
        if (validator == null) 
            validator = validatorFactoryBean.getValidator();
        return validator;
    }

}
