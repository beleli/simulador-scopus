package br.com.scopus.simulador.repository.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.jerimum.fw.entity.AbstractEntity;
import br.com.scopus.simulador.repository.entity.converter.TransactionTypeConverter;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "tb_cte_configuration")
public class Configuration extends AbstractEntity<Long>{

    private static final long serialVersionUID = -1548214849459613492L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuration", unique = true, nullable = false)
    private Long id;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "id_transaction_type", unique = true, nullable = false)
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType transactionType;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Size(min = 1, max = 40, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_name", length = 40, nullable = false)
    private String name;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "vl_port", nullable = false)
    private Integer port;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "vl_timeout", nullable = false)
    private Integer timeout;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "qt_bytes_access", nullable = false)
    private Integer bytesAccess;
    
    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public Long getPK() {
        return id;
    }

    public Configuration(Long id){
        this.id = id;
    }
}
