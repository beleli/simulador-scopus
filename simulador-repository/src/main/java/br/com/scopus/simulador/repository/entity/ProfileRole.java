package br.com.scopus.simulador.repository.entity;

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

import br.com.jerimum.fw.entity.AbstractEntity;
import br.com.scopus.simulador.repository.entity.converter.ProfileConverter;
import br.com.scopus.simulador.repository.entity.enums.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "tb_cte_profile_role")
public class ProfileRole extends AbstractEntity<Long> {

    private static final long serialVersionUID = -7683679618793945033L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profile_role", unique = true, nullable = false)
    private Long id;        
    
    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @Column(name = "id_profile", nullable = false)
    @Convert(converter = ProfileConverter.class)
    private Profile profile;

    @NotNull(message = "msg.erro.constraint.notnull.xxx")
    @ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", nullable = false)
    private Role role;

    public ProfileRole(Long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return String.format("Profile: %s; Role: %s", this.profile, this.role);
    }

    @Override
    public Long getPK() {
        return id;
    }

}
