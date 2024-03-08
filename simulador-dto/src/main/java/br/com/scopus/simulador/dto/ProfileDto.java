package br.com.scopus.simulador.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object para Perfis.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProfileDto implements Serializable {

    private static final long serialVersionUID = 4904521349760428130L;
    
    private Integer id;
    private String name;

}
