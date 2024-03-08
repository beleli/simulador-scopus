package br.com.scopus.simulador.repository.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@EqualsAndHashCode(callSuper = false, of = "pk")
@Entity
@Table(name = "vw_cte_transaction_parent")
public class TransactionParent extends AbstractEntity<Long> {

    private static final long serialVersionUID = 6501729151208057487L;

    @EmbeddedId
    private TransactionParentPK pk;
    
    @Column(name = "id_transaction_type", unique = true, nullable = false)
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType transactionType;

    @Column(name = "id_transaction_encoding", nullable = false)
    @Convert(converter = TransactionEncodingConverter.class)
    private TransactionEncoding transactionEncoding;

    @Column(name = "cd_identification", length = 8, nullable = false)
    private String identification;
    
    @Column(name = "ds_description", length = 30)
    private String description;
    
    @ManyToOne(targetEntity = Transaction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_transaction_parent")
    private Transaction transactionParent;
    
    @Column(name = "id_router_type")
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType routerType;
    
    @Column(name = "fl_parent", nullable = false)
    private boolean parent;
    
    @Column(name = "dt_last_access")
    private Date lastAccess;
    
    @Override
    public String getLabel() {
        return identification;
    }

    @Override
    public Long getPK() {
        return this.pk.getId();
    }
    
    public Long getLayoutOutputTransactionId() {
        return this.pk.getLayoutOutputTransactionId();
    }
    
    public Long getId() {
        return this.pk.getId();
    }

}
