package br.com.scopus.simulador.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe de entidade persistente para a chave da view vw_cte_transaction_parent.
 * 
 * @see http://docs.jboss.org/hibernate/validator/5.2/reference/en-US/html/
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TransactionParentPK implements Serializable {

    private static final long serialVersionUID = 5577308737776458142L;

    @Column(name = "id_transaction")
    private Long id;

    @Column(name = "id_layout_output_transaction")
    private Long layoutOutputTransactionId;

}
