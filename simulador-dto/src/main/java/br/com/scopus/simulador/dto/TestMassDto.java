package br.com.scopus.simulador.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Massa de Teste.
 * 
 * @author Yuslley Silva Fagundes - yfagundes@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestMassDto implements Serializable {

    private static final long serialVersionUID = 8162365968242755497L;

    private Long id;
    private Long testScenarioId;
    private Long layoutOutputTransactionId;
    private String transactionName;
    private Long projectId;
    private String description;
    private Integer errorAverage;
    private Integer timeout;
    private Integer occurrences;
    private String restart;
    private Integer returnCode;
    private Long userId;
    private List<TestMassItemDto> inputList;
    private List<TestMassItemDto> outputList;
}
