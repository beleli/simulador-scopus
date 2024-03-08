package br.com.scopus.simulador.repository.entity;

import java.util.Date;

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
import br.com.scopus.simulador.repository.entity.converter.TransactionEncodingConverter;
import br.com.scopus.simulador.repository.entity.converter.TransactionTypeConverter;
import br.com.scopus.simulador.repository.entity.enums.TransactionEncoding;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Classe de entidade persistente para a tabela tb_cte_transaction.
 * Tabela legada: tTransExistSmula
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
@Table(name = "tb_cte_transaction")
public class Transaction extends AbstractEntity<Long> {

    private static final long serialVersionUID = 9043491903620522233L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction", unique = true, nullable = false)
    private Long id;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "id_transaction_type", unique = true, nullable = false)
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType transactionType;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "id_transaction_encoding", nullable = false)
    @Convert(converter = TransactionEncodingConverter.class)
    private TransactionEncoding transactionEncoding;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Size(min = 1, max = 8, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "cd_identification", length = 8, nullable = false)
    private String identification;
    
    @Size(min = 0, max = 60, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_description", length = 60)
    private String description;
    
    @ManyToOne(targetEntity = Transaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_transaction_parent")
    private Transaction transactionParent;
    
    @Column(name = "id_router_type")
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType routerType;
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "fl_parent", nullable = false)
    private boolean parent;
    
    @Column(name = "dt_last_access")
    private Date lastAccess;
    
    public Transaction(Long id) {
        this.id = id;
    }
    
    @Override
    public String getLabel() {
        return identification;
    }

    @Override
    public Long getPK() {
        return id;
    }

}
