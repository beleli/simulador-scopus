package br.com.scopus.simulador.portal;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.springframework.data.rest.webmvc.json.Jackson2DatatypeHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Jackson2DatatypeHelper.configureObjectMapper(mapper);
        return mapper.writeValueAsBytes(object);
    }

    public static String convertObjectToJsonString(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Jackson2DatatypeHelper.configureObjectMapper(mapper);
        return mapper.writeValueAsString(object);
    }

    static HttpHeaders getHeaders(String userid, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(APPLICATION_JSON_UTF8));

        String auth = userid + ":" + password;

        byte[] encodedAuthorisation = Base64.encode(auth.getBytes());
        headers.add("Authorization", "Basic " + new String(encodedAuthorisation));

        return headers;
    }
}
