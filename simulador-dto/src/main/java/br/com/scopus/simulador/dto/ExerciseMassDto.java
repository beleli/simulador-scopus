package br.com.scopus.simulador.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Exercitar Massa de Teste.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMassDto implements Serializable{

    private static final long serialVersionUID = -6607056660035049643L;

    private Long layoutOutputTransactionId;
    private String transactionName;
    private Long transactionRouterId;
    private Integer transactionMechanismId;
    private Integer service;
    private String cics;
    private boolean securityHeader;
    private String user;
    private String password;
    private List<ExerciseMassItemDto>inputList;
    private List<ExerciseMassItemDto>outputList;
    private List<ExerciseMassItemDto>routerInputList;
    private List<ExerciseMassItemDto>routerOutputList;
}
