package br.com.scopus.simulador.portalws.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.jerimum.fw.aop.JerimumAspectLog;

/**
 * Log dos metodos da aplicacao via aspecto.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Aspect
@Component
@Order(100)
public class AspectLog  extends JerimumAspectLog {

    @Before("br.com.scopus.simulador.portalws.aop.AspectPointcuts.rest() || br.com.scopus.simulador.portalws.aop.AspectPointcuts.service()")
    @Override
    public void logEntry(JoinPoint jp) throws Exception {
        super.logEntry(jp);
    }

    @AfterReturning(pointcut = "(br.com.scopus.simulador.portalws.aop.AspectPointcuts.rest() || br.com.scopus.simulador.portalws.aop.AspectPointcuts.service()) && (execution(void *..*(..)))")
    @Override
    public void logExit(JoinPoint jp) throws Exception {
        super.logExit(jp);
    }

    @AfterReturning(pointcut = "(br.com.scopus.simulador.portalws.aop.AspectPointcuts.rest() || br.com.scopus.simulador.portalws.aop.AspectPointcuts.service()) && !(execution(void *..*(..)))", returning = "returningValue", argNames = "jp,returningValue")
    @Override
    public void logExit(JoinPoint jp, Object returningValue) throws Exception {
        super.logExit(jp, returningValue);
    }

    @AfterThrowing(pointcut = "br.com.scopus.simulador.portalws.aop.AspectPointcuts.rest() || br.com.scopus.simulador.portalws.aop.AspectPointcuts.service()", throwing = "ex", argNames = "jp,ex")
    @Override
    public void logException(JoinPoint jp, Throwable ex) throws Throwable {
        super.logException(jp, ex);
    }
}
