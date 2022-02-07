package ci.bhci.bhevaluationpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.bhci.bhevaluationpro.domain.PersonnelPoste;

/**
 * Repository for the PersonnelPoste entity that extends the
 * AbstractBaseRepository
 * 
 * @author kyao
 * @since 2022-02-03
 */

@Repository
public interface PersonnelPosteRepository extends AbstractBaseRepository<PersonnelPoste, Long> {

	@Query("select t from PersonnelPoste t where id_personnel_poste = :personelPosteId and isactive = 1 and is_deleted is null")
	Optional<PersonnelPoste> findById(@Param("personelPosteId") Long fonctionId);

	@Query("select p from PersonnelPoste p where isactive = 1 and is_deleted is null")
	List<PersonnelPoste> findAll();

	@Query("select p from PersonnelPoste p where id_fonction = :fonctionId and isactive = 1 and is_deleted is null")
	List<PersonnelPoste> findByFonction(@Param("fonctionId") Long fonctionId);

	@Query("select p from PersonnelPoste p where id_fonction = :personnelId and isactive = 1 and is_deleted is null")
	List<PersonnelPoste> findByPersonnel(@Param("personnelId") Long personnelId);

	@Query("select case when count(p)> 0 then true else false end from PersonnelPoste p where id_personnel = :personnelId and "
			+ "id_fonction = :fonctionId and isactive= 1 and is_deleted is null")
	boolean existPersonnelPosteQuery(@Param("personnelId") Long personnelId, @Param("fonctionId") Long fonctionId);
}