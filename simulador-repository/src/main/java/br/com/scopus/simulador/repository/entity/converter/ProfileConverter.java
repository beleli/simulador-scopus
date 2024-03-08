package br.com.scopus.simulador.repository.entity.converter;

import javax.persistence.Converter;
import org.springframework.stereotype.Component;

import br.com.scopus.simulador.repository.entity.enums.Profile;

/**
 * Conversor da classe Profile
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 *
 */
@Converter(autoApply = true)
@Component("profileConverter")
public class ProfileConverter extends GenericEnumConverter<Profile> {

    public ProfileConverter() {
        super(Profile.class);
    }

}
