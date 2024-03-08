package br.com.scopus.simulador.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.jerimum.fw.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Classe de entidade persistente para a tabela tb_role.
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
@Table(name = "tb_cte_role")
public class Role extends AbstractEntity<Long> {

    private static final long serialVersionUID = 5034413230314490833L;

    @Id
    @Column(name = "id_role", unique = true, nullable = false)
    private Long id;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Size(min = 1, max = 60, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_name", nullable = false, length = 60)
    private String name;

    public Role(Long id) {
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
