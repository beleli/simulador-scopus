package br.com.scopus.simulador.repository.entity;

import javax.persistence.Column;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Classe de entidade persistente para a tabela tb_cte_layout_version.
 * Tabela legada: tCondcLyoutResptSmula
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
@Table(name = "tb_cte_layout_version")
public class LayoutVersion extends AbstractEntity<Long> {

    private static final long serialVersionUID = 8709923105284053219L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_layout_version", unique = true, nullable = false)
    private Long id;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = LayoutInput.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_layout_input", nullable = false)
    LayoutInput layoutInput;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = LayoutOutputTransaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_layout_output_transaction", nullable = false)
    LayoutOutputTransaction layoutOutputTransaction;

    @Size(min = 0, max = 256, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_value", length = 256)
    String value;

    public LayoutVersion(Long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return layoutInput.getName();
    }

    @Override
    public Long getPK() {
        return id;
    }

}
