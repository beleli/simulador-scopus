package br.com.scopus.simulador.repository.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Tipos de usuário suportados pela aplicação.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@AllArgsConstructor
public enum Profile implements Enumerator {

	ADMINISTRADOR(1, "Administrador"),
	SUPORTE(2, "Suporte"),
	USUARIO(3, "Usuário");

    @Getter
    private Integer id;
    @Getter
    private String name;

}
