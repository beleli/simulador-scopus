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
 * Classe de entidade persistente para a tabela tb_cte_test_mass_output.
 * Tabela legada: tRegSaidaSmula
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
@Table(name = "tb_cte_test_mass_output")
public class TestMassOutput extends AbstractEntity<Long> {

    private static final long serialVersionUID = -8298703471876390609L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_test_mass_output", unique = true, nullable = false)
    private Long id;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = TestMass.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_test_mass", nullable = false)
    private TestMass testMass;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = LayoutOutput.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_layout_output", nullable = false)
    private LayoutOutput layoutOutput;

    @ManyToOne(targetEntity = LayoutInput.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_layout_input")
    private LayoutInput layoutInput;

    @Size(min = 0, max = 2048, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_value", length = 2048)
    String value;

    public TestMassOutput(Long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return value;
    }

    @Override
    public Long getPK() {
        return id;
    }

}
