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

import br.com.jerimum.fw.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Classe de entidade persistente para a tabela tb_cte_projetc_user.
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
@Table(name = "tb_cte_projetc_user")
public class ProjectUser extends AbstractEntity<Long> {

    private static final long serialVersionUID = 8214288758752794432L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project_user", unique = true, nullable = false)
    private Long id;       
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = Project.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_project", nullable = false)
    private Project project;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public ProjectUser(Long id) {
        this.id = id;
    }
    
    @Override
    public String getLabel() {
        return String.format("Project: %s; User: %s", this.project, this.user);
    }

    @Override
    public Long getPK() {
        return this.id;
    }
}
