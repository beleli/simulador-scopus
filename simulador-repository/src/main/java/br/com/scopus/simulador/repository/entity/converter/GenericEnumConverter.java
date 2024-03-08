package br.com.scopus.simulador.repository.entity.converter;

import java.util.EnumSet;

import javax.persistence.AttributeConverter;

import br.com.scopus.simulador.repository.entity.enums.Enumerator;

/**
 * Converte o enumerador para o Id de banco e o inverso.
 * 
 * @author Carlos Alberto Beleli Junior
 *
 */
public class GenericEnumConverter<E extends Enum<E> & Enumerator> implements AttributeConverter<Enumerator, Integer> {

    private Class<E> enumClass;
    
    public GenericEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E convertToEntityAttribute(Integer id) {
        for (E en : EnumSet.allOf(enumClass)) {
            if (en.getId().equals(id)) {
                return en;
            }
        }
        return null;
    }
    
    @Override
    public Integer convertToDatabaseColumn(Enumerator attribute) {
        return (attribute != null ? attribute.getId() : null);
    }
}