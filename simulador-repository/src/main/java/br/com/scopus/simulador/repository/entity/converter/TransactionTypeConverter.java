package br.com.scopus.simulador.repository.entity.converter;

import javax.persistence.Converter;
import org.springframework.stereotype.Component;

import br.com.scopus.simulador.repository.entity.enums.TransactionType;

/**
 * Conversor da classe TransactionType
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 *
 */
@Converter(autoApply = true)
@Component("transactionTypeConverter")
public class TransactionTypeConverter extends GenericEnumConverter<TransactionType> {

    public TransactionTypeConverter() {
        super(TransactionType.class);
    }

}
