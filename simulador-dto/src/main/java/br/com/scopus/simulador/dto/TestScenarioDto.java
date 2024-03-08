package br.com.scopus.simulador.dto;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Senário de Teste.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestScenarioDto implements Serializable {
    private static final long serialVersionUID = 8162365968242755497L;

    private Long id;
    private String description;
    private Long transactionId;
    private String transactionName;
    private String transactionTypeName;

}
