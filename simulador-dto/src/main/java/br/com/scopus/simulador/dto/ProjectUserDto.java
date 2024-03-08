package br.com.scopus.simulador.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Usuários de projeto.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
public class ProjectUserDto implements Serializable {

    private static final long serialVersionUID = 4418219501309575205L;

    private Long id;
    private Long userId;
    private Long projectId;
}
