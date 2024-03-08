package br.com.scopus.simulador.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Transações.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
public class TransactionDto implements Serializable {
    
    private static final long serialVersionUID = -8971158640159404133L;
    private Long id;
    private Integer transactionTypeId;
    private String transactionTypeName;
    private Integer transactionEncodingId;
    private String transactionEncodingName;
    private String name;
    private String description;
    private Long transactionParentId;
    private Integer routerTypeId;
    private boolean parent;
    private Date lastAccess;
    private Long transactionOutputId;
    private String transactionOutputDescription;
    private boolean multipleOutputs;
    private List<TransactionItemDto> inputFields = new ArrayList<TransactionItemDto>();
    private List<TransactionItemDto> outputFields = new ArrayList<TransactionItemDto>();
    private Integer qtdBytesInput;
    private Integer qtdBytesOutput;
    private Long userId;
}
