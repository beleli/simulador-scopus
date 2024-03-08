package br.com.scopus.simulador.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false, of = "id")
public class TransactionItemDto implements Serializable {

    private static final long serialVersionUID = -8726477363911058906L;

    private Long id;
    private Integer fieldTypeId;
    private String fieldTypeName;
    private String name;
    private Integer size;
    private Integer ordinal;
    private boolean version;
    private String value;
    private boolean parent;

}
