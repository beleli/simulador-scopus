package br.com.scopus.simulador.repository.entity.converter;

import javax.persistence.Converter;
import org.springframework.stereotype.Component;

import br.com.scopus.simulador.repository.entity.enums.FieldType;

/**
 * Conversor da classe FieldType
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 *
 */
@Converter(autoApply = true)
@Component("fieldTypeConverter")
public class FieldTypeConverter extends GenericEnumConverter<FieldType> {

    public FieldTypeConverter() {
        super(FieldType.class);
    }

}
