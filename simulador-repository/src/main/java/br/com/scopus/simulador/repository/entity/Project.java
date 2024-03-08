package br.com.scopus.simulador.repository.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * Classe de entidade persistente para a tabela tb_cte_project.
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
@Table(name = "tb_cte_project")
public class Project extends AbstractEntity<Long> {
    
    private static final long serialVersionUID = 7454889455033632700L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project", unique = true, nullable = false)
    private Long id;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Size(min = 1, max = 32, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "cd_project", unique = true, nullable = false, length = 32)
    private String code;
    
    @Size(min = 0, max = 256, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_project", nullable = true, length = 256)
    private String description;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "dt_begin", nullable = false)
    private Date beginDate;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "dt_end", nullable = false)
    private Date endDate;
    
    public Project(Long id) {
        this.id = id;
    }
    
    @Override
    public String getLabel() {
        return code;
    }

    @Override
    public Long getPK() {
        return id;
    }

}
