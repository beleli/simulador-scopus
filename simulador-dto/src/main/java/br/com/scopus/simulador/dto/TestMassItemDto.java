package br.com.scopus.simulador.dto;

import java.io.Serializable;
import java.util.List;

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
public class TestMassItemDto implements Serializable {

    private static final long serialVersionUID = 8982005684583340702L;
    
    private Long id;
    private Long layoutId;
    private String name;
    private Integer size;
    private String type;
    private String value;
    private Integer ordinal;
    private boolean readyOnly;
    private Long copyFieldId;
    private List<ComboBoxDto> actions;
}
