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
 * Classe de entidade persistente para a tabela tb_cte_layout_output_transaction.
 * Tabela legada: tLyoutResptSmula
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
@Table(name = "tb_cte_layout_output_transaction")
public class LayoutOutputTransaction extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1233043312202639944L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_layout_output_transaction", unique = true, nullable = false)
    private Long id;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = Transaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_transaction", nullable = false)
    private Transaction transaction;

    @Size(min = 0, max = 60, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_description", length = 60)
    private String description;

    public LayoutOutputTransaction(Long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return description;
    }

    @Override
    public Long getPK() {
        return id;
    }

}
