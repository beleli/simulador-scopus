package br.com.scopus.simulador.repository.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Tipos de codificação suportados pela aplicação.
 * Tabela legada: tFormtApdorSmula
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@AllArgsConstructor
public enum TransactionEncoding implements Enumerator {

    EBCDIC(1, "EBCDIC"),
    ASCII(2, "ASCII");

    @Getter
    private Integer id;
    @Getter
    private String name;

}
