package ci.bhci.bhevaluationpro.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain Direction
 * 
 * @author kyao
 * @since 2022-02-02
 */

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_direction")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "modifiedAt", "deletedAt" }, allowGetters = true)
public class Direction extends AbstractBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_direction")
	private Long id;
	@Column(name = "code_direction")
	private String codeDirection;
	@Column(name = "libelle_direction")
	private String libelleDirection;
	@OneToMany(mappedBy = "direction", cascade = CascadeType.ALL)
	private List<Departement> departements = new ArrayList<>();
	@OneToMany(mappedBy = "direction", cascade = CascadeType.ALL)
	private List<Fonction> fonctions = new ArrayList<>();
}
