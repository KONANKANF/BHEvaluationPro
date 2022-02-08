package ci.bhci.bhevaluationpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.bhci.bhevaluationpro.domain.Departement;

/**
 * Repository for the Departement entity that extends the AbstractBaseRepository
 * 
 * @author kyao
 * @since 2022-02-03
 */

@Repository
public interface DepartementRepository extends AbstractBaseRepository<Departement, Long> {

	@Query("select case when count(d)> 0 then true else false end from Departement d where id_direction = :directionId and lower(libelle_service) like lower(:libelle) and isactive= 1 and is_deleted is null")
	boolean existDepartement(@Param("directionId") Long directionId, @Param("libelle") String libelleDepartement);

	@Query("select d from Departement d where id_service = :departementId and isactive = 1 and is_deleted is null")
	Optional<Departement> findById(@Param("departementId") Long departementId);

	@Query("select d from Departement d where lower(libelle_departement) like lower(:libelle) and isactive= 1 and is_deleted is null")
	Optional<Departement> findByLibelleDepartement(@Param("libelle") String libelleDepartement);

	@Query("select d from Departement d where isactive = 1 and is_deleted is null")
	List<Departement> findAll();
}