package ci.bhci.bhevaluationpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.bhci.bhevaluationpro.domain.Direction;

/**
 * Repository for the Direction entity that extends the AbstractBaseRepository
 * 
 * @author kyao
 * @since 2022-02-03
 */

@Repository
public interface DirectionRepository extends AbstractBaseRepository<Direction, Long> {

	@Query("select d from Direction d where id_direction = :id and isactive = 1 and is_deleted is null")
	Optional<Direction> findById(@Param("id") Long directionId);
	
	@Query("select d from Direction d where lower(libelle_direction) like lower(:libelle) and isactive= 1 and is_deleted is null")
	Optional<Direction> findByLibelleDirection(@Param("libelle") String libelleDirection);

	@Query("select d from Direction d where isactive = 1 and is_deleted is null")
	List<Direction> findAll();
	
	@Query("select case when count(d)> 0 then true else false end from Direction d where lower(libelle_direction) like lower(:libelle) and lower(code_direction) like lower(:code) and isactive= 1 and is_deleted is null")
	boolean existDirection(@Param("code") String codeDirection, @Param("libelle") String libelleDirection);
}