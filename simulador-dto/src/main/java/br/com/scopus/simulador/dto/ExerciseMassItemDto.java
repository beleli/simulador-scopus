package br.com.scopus.simulador.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para campo da transação.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMassItemDto  implements Serializable {

    private static final long serialVersionUID = -3332880196824045654L;
    
    private Long layoutId;
    private String name;
    private Integer size;
    private String type;
    private String value;
    private Integer ordinal;
    private boolean readyOnly;
}
