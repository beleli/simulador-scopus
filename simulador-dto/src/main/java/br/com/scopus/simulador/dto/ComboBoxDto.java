package br.com.scopus.simulador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object com id e nome.
 * 
 * @author Carlos Alberto Beleli Junior
 * @since 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = { "id" })
public class ComboBoxDto {

    private Long id;
    private String name;
    
}