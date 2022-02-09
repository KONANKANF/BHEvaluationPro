package ci.bhci.bhevaluationpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.bhci.bhevaluationpro.domain.Personnel;

@Repository
public interface PersonnelRepository extends AbstractBaseRepository<Personnel, Long> {

	@Query("select p from Personnel p where id_personnel = :personnelId and isactive = 1 and is_deleted is null")
	Optional<Personnel> findById(@Param("personnelId") Long personnelId);

	@Query("select p from Personnel p where isactive = 1 and is_deleted is null")
	List<Personnel> findAll();

	@Query("select case when count(p)> 0 then true else false end from Personnel p "
			+ "where lower(matricule_personnel) like lower(:matricule) and lower(email_personnel) like lower(:email) "
			+ "and isactive= 1 and is_deleted is null")
	boolean existPersonnelLikeCustomQuery(@Param("matricule") String matricule, @Param("email") String email);
	
	@Query("select p from Personnel p where id_personnel_poste = :personnelPosteId and isactive= 1 and is_deleted is null")
	List<Personnel> findByPersonnelPostes(@Param("personnelPosteId") Long personnelPosteId);
	
}