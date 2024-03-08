package br.com.scopus.simulador.dto;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Alteracao de Senha.
 * 
 * @author Carlos Alberto Beleli Junior
 * @since 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AlterarSenhaDto implements Serializable {

    private static final long serialVersionUID = 6791514461229575618L;

    @Getter
    private String email;
    @Getter
    private String password;
    @Getter
    private String newPassword;
    
}
