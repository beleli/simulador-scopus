package br.com.scopus.simulador.repository.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Tipos de campo suportados pela aplicação.
 * Tabela legada: tTpoCpoApdor
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@AllArgsConstructor
public enum FieldType implements Enumerator {

    ALPHANUMERIC(1, "Alfanumerico"),
    BYTE(2, "Byte"),
    DECIMAL_COMPRESSED(3, "Decimal Compactado"),
    DECIMAL_ZONED(4, "Decimal Zonado");

    @Getter
    private Integer id;
    @Getter
    private String name;
    
}
