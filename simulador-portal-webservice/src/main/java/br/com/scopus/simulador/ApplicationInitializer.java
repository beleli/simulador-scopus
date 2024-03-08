package br.com.scopus.simulador;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import br.com.jerimum.fw.config.JerimumWebApplicationInitializer;

@Order(1)
public class ApplicationInitializer extends JerimumWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public Class<?> getConfigurationClass() {
        return Application.class;
    }

    @Override
    public String getEnvironmentJVMParam() {
        return "simulador.portal.webservice.config.dir";
    }

}
