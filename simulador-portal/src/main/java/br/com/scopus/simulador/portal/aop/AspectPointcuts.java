package br.com.scopus.simulador.portal.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Definicao dos pointcuts para os aspectos da aplicacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Aspect
@Component
public class AspectPointcuts {

    @Pointcut("execution(* br.com.scopus.simulador.portal.rest.*Controller.*(..))")
    public void rest() {
        // Do nothing, only for pointcut definition
    }
}
