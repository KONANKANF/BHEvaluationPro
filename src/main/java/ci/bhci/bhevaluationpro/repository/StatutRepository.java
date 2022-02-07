package ci.bhci.bhevaluationpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.bhci.bhevaluationpro.domain.Statut;

/**
 * Repository for the Statut entity that extends the AbstractBaseRepository
 * 
 * @author kyao
 * @since 2022-02-03
 */

@Repository
public interface StatutRepository extends AbstractBaseRepository<Statut, Long> {
	@Query("select case when count(s)> 0 then true else false end from Statut s where lower(libelle_statut) like lower(:libelle) and isactive= 1 and is_deleted is null")
	boolean existStatut(@Param("libelle") String libelleStatut);

	@Query("select s from Statut s where id_statut = :statutId and isactive = 1 and is_deleted is null")
	Optional<Statut> findById(@Param("statutId") Long fonctionId);
	
	@Query("select s from Statut s where lower(libelle_statut) like lower(:libelle) and isactive= 1 and is_deleted is null")
	Optional<Statut> findByLibelleStatut(@Param("libelle") String libelleStatut);

	@Query("select s from Statut s where isactive = 1 and is_deleted is null")
	List<Statut> findAll();
}
