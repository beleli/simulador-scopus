package br.com.scopus.simulador.portal.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.reflect.TypeToken;

import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.jerimum.fw.json.JSONUtils;
import br.com.jerimum.fw.logging.LoggerUtils;
import br.com.scopus.simulador.dto.ResponseDto;
import br.com.scopus.simulador.dto.RoleUserDto;
import br.com.scopus.simulador.dto.UserDto;
import br.com.scopus.simulador.portal.config.ApplicationConfigRestService;

/**
 * Ponto de entrada para o login do usuario na aplicacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Component
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApplicationConfigRestService appConfigRest;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            ResponseDto<UserDto> dto = findUserByEmail(email);
            if (dto != null && dto.getContent() != null) {
                if (dto.getContent().getEnabled()) {
                    /* recupera as permissoes de acesso do usuario */
                    List<GrantedAuthority> authorities = getAuthorities(dto.getContent());
                    return new UsuarioLogado(dto.getContent(), authorities);

                } else {
                    /* usuario inativo */
                    String msg = I18nUtils.getMsg(this.messageSource, "msg.usuario.inativo");
                    throw new AuthenticationServiceException(msg);
                }
            }

        } catch (AuthenticationServiceException e) {
            throw e;
        } catch (Exception e) {
            LoggerUtils.logError(this.getClass(), "Falha ao realizar login", e);
        }

        /* usuario nao encontrado */
        String msg = I18nUtils.getMsg(this.messageSource, "msg.usuario.nao.encontrado");
        throw new UsernameNotFoundException(msg);
    }

    /**
     * Consulta as permissoes do usuario informado como parametro.
     * 
     * @param user
     * @return {@link List}
     */
    private List<GrantedAuthority> getAuthorities(UserDto user) {

        ResponseDto<RoleUserDto> dto = findRoleUserByProfile(user);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        if ( dto != null && dto.getContent() != null) {
        
        for(String item : dto.getContent().getListRole()) {
            authorities.add(new SimpleGrantedAuthority(item));
        }
        
        }
        return authorities;
    }

    private ResponseDto<UserDto> findUserByEmail(String email) {

        LoggerUtils.logDebug(this.getClass(), "Realizando chamada GET a url {} ",
            appConfigRest.getUserFindByEmailWebSerivceURL());
        /*
         * Executa a chamada ao servico.
         */
        String mensagemRetornoJson = restTemplate.getForObject(appConfigRest.getUserFindByEmailWebSerivceURL(),
            String.class, email);
        LoggerUtils.logDebug(this.getClass(), "Retorno do servico: {}", mensagemRetornoJson);

        /*
         * Transforma o retorno do servico para o DTO de retorno.
         */
        return JSONUtils.deserialize(new TypeToken<ResponseDto<UserDto>>() {
        }, mensagemRetornoJson);

    }
    
    private ResponseDto<RoleUserDto> findRoleUserByProfile(UserDto user) {
        
        LoggerUtils.logDebug(this.getClass(), "Realizando chamada GET a url {} ",
            appConfigRest.getUserFindByEmailWebSerivceURL());
        /*
         * Executa a chamada ao servico.
         */
        String mensagemRetornoJson = restTemplate.getForObject(appConfigRest.getUserFindRoleUserByProfileWebSerivceURL(),
            String.class, user.getIdProfile());
        LoggerUtils.logDebug(this.getClass(), "Retorno do servico: {}", mensagemRetornoJson);

        /*
         * Transforma o retorno do servico para o DTO de retorno.
         */
        return JSONUtils.deserialize(new TypeToken<ResponseDto<RoleUserDto>>() {
        }, mensagemRetornoJson);
        
    }

}
