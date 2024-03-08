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
 * Classe de entidade persistente para a tabela tb_cte_test_mass.
 * Tabela legada: tContdTesteSmula
 * 
 * @see http://docs.jboss.org/hibernate/validator/5.2/reference/en-US/html/
 * @author Yuslley Silva Fagundes- yfagundes@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "tb_cte_test_mass")
public class TestMass extends AbstractEntity<Long>{

    private static final long serialVersionUID = 8214288758752794432L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_test_mass", unique = true, nullable = false)
    private Long id;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = TestScenario.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_test_scenario", nullable = false)
    private TestScenario testScenario;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = LayoutOutputTransaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_layout_output_transaction", nullable = false)
    private LayoutOutputTransaction layoutOutputTransaction;
    
    @ManyToOne(targetEntity = Project.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_project", nullable = true)
    private Project project;
    
    @Size(min = 0, max = 60, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_description", length = 60)
    private String description;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "vl_error_average", nullable = false)
    private Integer errorAverage;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "vl_timeout", nullable = false)
    private Integer timeout;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "vl_occurrences", nullable = false)
    private Integer occurrences;
    
    @Size(min = 0, max = 255, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_restart", length = 255)
    private String restart;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "vl_return_code", nullable = false)
    private Integer returnCode;
    
    @Override
    public String getLabel() {
        return description;
    }

    @Override
    public Long getPK() {
        return id;
    }
    
    public TestMass(Long id){
        this.id = id;
    };
    
}
