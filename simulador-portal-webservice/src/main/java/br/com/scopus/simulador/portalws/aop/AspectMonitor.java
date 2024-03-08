package br.com.scopus.simulador.portalws.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.jerimum.fw.aop.JerimumAspectMonitor;

/**
 * Monitoramento dos metodos da aplicacao via aspecto.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Aspect
@Component
@Order(200)
public class AspectMonitor extends JerimumAspectMonitor {

    @Around("br.com.scopus.simulador.portalws.aop.AspectPointcuts.rest() || br.com.scopus.simulador.portalws.aop.AspectPointcuts.service()")
    @Override
    protected Object monitor(ProceedingJoinPoint jp) throws Throwable {
        return super.monitor(jp);
    }

}
