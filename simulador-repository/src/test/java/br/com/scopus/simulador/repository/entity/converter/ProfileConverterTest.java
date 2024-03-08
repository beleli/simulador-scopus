package br.com.scopus.simulador.repository.entity.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import br.com.scopus.simulador.repository.entity.converter.ProfileConverter;
import br.com.scopus.simulador.repository.entity.enums.Profile;

public class ProfileConverterTest {
	ProfileConverter converter;
    
    @Before
    public void setUp() throws Exception {
        converter = new ProfileConverter();
    }

    @Test
    public void testConvertToEntityAttribute() {
        Profile value = converter.convertToEntityAttribute(Profile.ADMINISTRADOR.getId());
        assertThat(value, equalTo(Profile.ADMINISTRADOR));
    }

    @Test
    public void testConvertToDatabaseColumn() {
        Integer id = converter.convertToDatabaseColumn(Profile.ADMINISTRADOR);
        assertThat(id, equalTo(Profile.ADMINISTRADOR.getId()));
    }

}
