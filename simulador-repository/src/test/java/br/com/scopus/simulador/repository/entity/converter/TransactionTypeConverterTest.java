package br.com.scopus.simulador.repository.entity.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import br.com.scopus.simulador.repository.entity.converter.TransactionTypeConverter;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;

public class TransactionTypeConverterTest {
        TransactionTypeConverter converter;
    
    @Before
    public void setUp() throws Exception {
        converter = new TransactionTypeConverter();
    }

    @Test
    public void testConvertToEntityAttribute() {
        TransactionType value = converter.convertToEntityAttribute(TransactionType.CICS.getId());
        assertThat(value, equalTo(TransactionType.CICS));
    }

    @Test
    public void testConvertToDatabaseColumn() {
        Integer id = converter.convertToDatabaseColumn(TransactionType.CICS);
        assertThat(id, equalTo(TransactionType.CICS.getId()));
    }

}
