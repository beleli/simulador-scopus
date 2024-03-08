package br.com.scopus.simulador.repository.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Tipos de mecanismos de simulação suportados pela aplicação.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@AllArgsConstructor
public enum Mechanism implements Enumerator {

    MAINFRAME(1, "Mainframe"),
    ISD(2, "Simulador - ISD 3.0"),
    SERVTRANS(3, "Simulador - ServTrans");

    @Getter
    private Integer id;
    @Getter
    private String name;
    
}
