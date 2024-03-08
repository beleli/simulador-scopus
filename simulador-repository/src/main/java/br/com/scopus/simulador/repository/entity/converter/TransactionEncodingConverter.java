package br.com.scopus.simulador.repository.entity.converter;

import javax.persistence.Converter;
import org.springframework.stereotype.Component;

import br.com.scopus.simulador.repository.entity.enums.TransactionEncoding;

/**
 * Conversor da classe TransactionEncoding
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 *
 */
@Converter(autoApply = true)
@Component("transactionEncodingConverter")
public class TransactionEncodingConverter extends GenericEnumConverter<TransactionEncoding> {

    public TransactionEncodingConverter() {
        super(TransactionEncoding.class);
    }

}
