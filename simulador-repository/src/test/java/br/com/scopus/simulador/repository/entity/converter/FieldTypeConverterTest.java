package br.com.scopus.simulador.repository.entity.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import br.com.scopus.simulador.repository.entity.converter.FieldTypeConverter;
import br.com.scopus.simulador.repository.entity.enums.FieldType;

public class FieldTypeConverterTest {
        FieldTypeConverter converter;
    
    @Before
    public void setUp() throws Exception {
        converter = new FieldTypeConverter();
    }

    @Test
    public void testConvertToEntityAttribute() {
        FieldType value = converter.convertToEntityAttribute(FieldType.ALPHANUMERIC.getId());
        assertThat(value, equalTo(FieldType.ALPHANUMERIC));
    }

    @Test
    public void testConvertToDatabaseColumn() {
        Integer id = converter.convertToDatabaseColumn(FieldType.ALPHANUMERIC);
        assertThat(id, equalTo(FieldType.ALPHANUMERIC.getId()));
    }

}
