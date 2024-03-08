package br.com.scopus.simulador.repository.entity.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import br.com.scopus.simulador.repository.entity.converter.TransactionEncodingConverter;
import br.com.scopus.simulador.repository.entity.enums.TransactionEncoding;

public class TransactionEncodingConverterTest {
        TransactionEncodingConverter converter;
    
    @Before
    public void setUp() throws Exception {
        converter = new TransactionEncodingConverter();
    }

    @Test
    public void testConvertToEntityAttribute() {
        TransactionEncoding value = converter.convertToEntityAttribute(TransactionEncoding.EBCDIC.getId());
        assertThat(value, equalTo(TransactionEncoding.EBCDIC));
    }

    @Test
    public void testConvertToDatabaseColumn() {
        Integer id = converter.convertToDatabaseColumn(TransactionEncoding.EBCDIC);
        assertThat(id, equalTo(TransactionEncoding.EBCDIC.getId()));
    }

}
