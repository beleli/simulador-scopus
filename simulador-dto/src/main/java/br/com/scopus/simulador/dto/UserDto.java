package br.com.scopus.simulador.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object para Usuarios.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
public class UserDto implements Serializable {

    private static final long serialVersionUID = -6371821750984243950L;
    
    @Getter
    @Setter
    private Long id;
    @Getter
    private Integer idProfile;
    @Getter
    private String name;
    @Getter
    private String nameProfile;
    @Getter
    private String email;
    @Getter
    private Boolean enabled;
    @Getter
    private Boolean changePassword;
    @Getter
    private String password;

}
