package br.com.scopus.simulador.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Configuração.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
public class ConfigurationDto implements Serializable {

    private static final long serialVersionUID = -2236030768602229554L;
    
    private Long id;
    private Integer transactionTypeId;
    private String transactionTypeName;
    private String name;
    private Integer port;
    private Integer timeout;
    private Integer bytesAccess;
}
