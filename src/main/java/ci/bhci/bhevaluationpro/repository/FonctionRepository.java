package ci.bhci.bhevaluationpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.bhci.bhevaluationpro.domain.Fonction;

/**
 * Repository for the Fonction entity that extends the AbstractBaseRepository
 * 
 * @author kyao
 * @since 2022-02-03
 */

@Repository
public interface FonctionRepository extends AbstractBaseRepository<Fonction, Long> {

	@Query("select case when count(f)> 0 then true else false end from Fonction f "
			+ "where (id_service = :departementId or id_service is null) and (manager_id_fonction = :managerIdFonction or manager_id_fonction is  null) "
			+ "and (id_direction = :directionId and lower(libelle_fonction) like lower(:libelle) and isactive= 1 and is_deleted is null)")
	boolean existFonction(@Param("directionId") Long directionId, @Param("departementId") Long departementId,
			@Param("managerIdFonction") Long managerIdFonction, @Param("libelle") String libelleFonction);

	@Query("select f from Fonction f where id_fonction = :fonctionId and isactive = 1 and is_deleted is null")
	Optional<Fonction> findById(@Param("fonctionId") Long fonctionId);

	@Query("select f from Fonction f where lower(libelle_fonction) like lower(:libelle) and isactive= 1 and is_deleted is null")
	Optional<Fonction> findByLibelleFonctionn(@Param("libelle") String libelleFonction);

	@Query("select f from Fonction f where isactive = 1 and is_deleted is null")
	List<Fonction> findAll();

	@Query("select f from Fonction f where manager_id_fonction = :managerIdFonction and isactive= 1 and is_deleted is null")
	List<Fonction> findByManager(@Param("managerIdFonction") Long managerIdFonction);

	@Query("select f from Fonction f where id_service = :departementeId and isactive= 1 and is_deleted is null")
	List<Fonction> findByDepartement(@Param("departementeId") Long departementeId);

	@Query("select f from Fonction f where id_direction = :directionId and isactive= 1 and is_deleted is null")
	List<Fonction> findByDirection(@Param("directionId") Long directionId);
}