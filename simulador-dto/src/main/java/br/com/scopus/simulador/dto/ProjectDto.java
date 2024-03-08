package br.com.scopus.simulador.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Projetos.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
public class ProjectDto implements Serializable {

    private static final long serialVersionUID = 3826936467113304900L;

    private Long id;
    private String code;
    private String description;
    private Date beginDate;
    private Date endDate;
    private List<ComboBoxDto> users;
    
    
}