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

import org.hibernate.validator.constraints.Email;

import br.com.jerimum.fw.entity.AbstractEntity;
import br.com.scopus.simulador.repository.entity.converter.ProfileConverter;
import br.com.scopus.simulador.repository.entity.enums.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Classe de entidade persistente para a tabela tb_cte_user.
 * 
 * @see http://docs.jboss.org/hibernate/validator/5.2/reference/en-US/html/
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "tb_cte_user")
public class User extends AbstractEntity<Long> {

    private static final long serialVersionUID = 7862590251185746050L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", unique = true, nullable = false)
    private Long id;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "id_profile", nullable = false)
    @Convert(converter = ProfileConverter.class)
    private Profile profile;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Size(min = 1, max = 60, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_name", nullable = false, length = 60)
    private String name;

    @Email(message = "msg.erro.constraint.email.xxx")
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Size(min = 1, max = 255, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_email", nullable = false, length = 255)
    private String email;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "fl_enabled", nullable = false)
    private Boolean enabled;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "fl_change_password", nullable = false)
    private Boolean changePassword;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Size(min = 1, max = 255, message = "msg.erro.constraint.maxsize.xxx")
    @Column(name = "ds_password", nullable = false, length = 255)
    private String password;

    public User(Long id){
        this.id = id;
    }
    
    @Override
    public Long getPK() {
        return this.id;
    }

    @Override
    public String getLabel() {
        return this.name;
    }

}
