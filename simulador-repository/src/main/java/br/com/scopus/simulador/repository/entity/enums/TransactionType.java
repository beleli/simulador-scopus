package br.com.scopus.simulador.repository.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Tipos de transações suportados pela aplicação.
 * Tabela legada: tTpoTransSmula
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@AllArgsConstructor
public enum TransactionType implements Enumerator {

    CICS(1, "CICS", "EA", 4),
    IMS(2, "IMS", "EA", 8),
    CWS(3, "CWS", "EA", 8),
    SOCKET_BRADESCARD(4, "Socket Bradescard", "AA", 4);

    @Getter
    private Integer id;
    @Getter
    private String name;
    @Getter
    private String codification;
    @Getter
    private Integer identificationBytes;

}
