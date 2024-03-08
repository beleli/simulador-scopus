package br.com.scopus.simulador.repository.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.jerimum.fw.entity.AbstractEntity;
import br.com.scopus.simulador.repository.entity.converter.FieldTypeConverter;
import br.com.scopus.simulador.repository.entity.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Classe de entidade persistente para a tabela tb_cte_layout_output.
 * Tabela legada: tCpoResptLyoutSmula
 * 
 * @see http://docs.jboss.org/hibernate/validator/5.2/reference/en-US/html/
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "tb_cte_layout_output")
public class LayoutOutput extends AbstractEntity<Long> {

    private static final long serialVersionUID = -9052504442943671603L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_layout_output", unique = true, nullable = false)
    private Long id;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = LayoutOutputTransaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_layout_output_transaction", nullable = false)
    private LayoutOutputTransaction layoutOutputTransaction;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "id_field_type", nullable = false)
    @Convert(converter = FieldTypeConverter.class)
    private FieldType fieldType;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Size(min = 1, max = 30, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_name", length = 30, nullable = false)
    private String name;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "vl_size", nullable = false)
    private Integer size;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "cd_ordinal", nullable = false)
    private Integer ordinal;
    
    public LayoutOutput(Long id) {
        this.id = id;
    }
    
    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public Long getPK() {
        return id;
    }

}
