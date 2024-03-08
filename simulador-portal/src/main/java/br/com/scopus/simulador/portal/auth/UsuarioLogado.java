package br.com.scopus.simulador.portal.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.scopus.simulador.dto.UserDto;
import lombok.Getter;

/**
 * Representa o usuario logado na aplicacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public class UsuarioLogado extends User {

    private static final long serialVersionUID = 2569274078970792797L;

    @Getter
    private Long id;

    @Getter
    private boolean ativo;

    @Getter
    private boolean firstAccess;

    @Getter
    private String name;

    @Getter
    private String nameProfile;

    public UsuarioLogado(UserDto usuarioDto, Collection<? extends GrantedAuthority> authorities) {
        super(usuarioDto.getEmail(), usuarioDto.getPassword(), authorities);
        this.id = usuarioDto.getId();
        this.ativo = usuarioDto.getEnabled();
        this.firstAccess = usuarioDto.getChangePassword();
        this.name = usuarioDto.getName();
        this.nameProfile = usuarioDto.getNameProfile();
    }

}
