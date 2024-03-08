package br.com.scopus.simulador.portal.rest;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import com.google.gson.reflect.TypeToken;

import br.com.jerimum.fw.json.JSONUtils;
import br.com.jerimum.fw.logging.LoggerUtils;

public abstract class SimuladorController<T> {

    @Autowired
    private RestTemplate restTemplate;

    protected <MSG> MSG chamadaPostWebService(T mensagemEnvio, TypeToken<MSG> tipoRetorno, String url) {

        LoggerUtils.logDebug(this.getClass(), "Realizando chamada POST a url {} ", url, getLoggedUsername());

        /*
         * Transforma a mensagem de envio para o JSON.
         */
        String mensagemEnvioJson = JSONUtils.serialize(mensagemEnvio);

        /*
         * Executa a chamada ao servico.
         */
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType( "application" , "json" , Charset.forName( "UTF-8" ) );
        headers.setContentType(mediaType);
        HttpEntity<String> entity = new HttpEntity<String>(mensagemEnvioJson, headers);

        String mensagemRetornoJson = restTemplate.postForObject(url, entity, String.class);
        LoggerUtils.logDebug(this.getClass(), "Retorno do servico: {}", mensagemRetornoJson);

        /*
         * Transforma o retorno do servico para o DTO de retorno.
         */
        return JSONUtils.deserialize(tipoRetorno, mensagemRetornoJson);
    }

    protected <MSG> MSG chamadaGetWebService(Object param, TypeToken<MSG> tipoRetorno, String url) {

        LoggerUtils.logDebug(this.getClass(), "Realizando chamada GET a url {} ", url, getLoggedUsername());

        /*
         * Executa a chamada ao servico.
         */

        String mensagemRetornoJson = restTemplate.getForObject(url, String.class, param);
        LoggerUtils.logDebug(this.getClass(), "Retorno do servico: {}", mensagemRetornoJson);

        /*
         * Transforma o retorno do servico para o DTO de retorno.
         */
        return JSONUtils.deserialize(tipoRetorno, mensagemRetornoJson);
    }
    
    
    protected <MSG> MSG chamadaGetWebService(TypeToken<MSG> tipoRetorno, String url) {

        LoggerUtils.logDebug(this.getClass(), "Realizando chamada GET a url {} ", getLoggedUsername(), url);

        /*
         * Executa a chamada ao servico.
         */
        String mensagemRetornoJson = restTemplate.getForObject(url, String.class);
        LoggerUtils.logDebug(this.getClass(), "Retorno do servico: {}", mensagemRetornoJson);

        /*
         * Transforma o retorno do servico para o DTO de retorno.
         */
        return JSONUtils.deserialize(tipoRetorno, mensagemRetornoJson);
    }
    
    /**
     * Returns the logged username.
     * 
     * @return {@link String}
     */
    protected String getLoggedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null ? auth.getName() : null);
    }
}
