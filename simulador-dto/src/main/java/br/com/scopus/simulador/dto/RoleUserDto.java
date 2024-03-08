package br.com.scopus.simulador.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Usuarios.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class RoleUserDto implements Serializable {

    private static final long serialVersionUID = 5635196040900483841L;
    @Getter
    private List<String> listRole;   
}
