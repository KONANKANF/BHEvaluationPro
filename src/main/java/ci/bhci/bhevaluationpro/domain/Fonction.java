package ci.bhci.bhevaluationpro.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain Fonction
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
@Table(name = "t_fonction")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "modifiedAt", "deletedAt" }, allowGetters = true)
public class Fonction extends AbstractBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fonction")
	private Long id;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_direction")
	private Direction direction;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_service")
	private Departement departement;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "manager_id_fonction")
	private Fonction managerIdFonction;
	@OneToMany(mappedBy = "fonction", cascade = CascadeType.ALL)
	private List<PersonnelPoste> personnelPostes = new ArrayList<>();
	@Column(name = "debut_poste")
	private Date debut_poste;
	@Column(name = "fin_poste")
	private Date fin_poste;
}
