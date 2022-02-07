/**
 * 
 */
package ci.bhci.bhevaluationpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.bhci.bhevaluationpro.domain.TypeEvaluation;

/**
 * Repository for the TypeEvaluation entity that extends the AbstractBaseRepository
 * 
 * @author kyao
 * @since 2022-02-03
 */

@Repository
public interface TypeEvaluationRepository extends AbstractBaseRepository<TypeEvaluation, Long> {

	@Query("select case when count(t)> 0 then true else false end from TypeEvaluation t where lower(libelle_type_evaluation) like lower(:libelle) and isactive= 1 and is_deleted is null")
	boolean existTypeEvaluation(@Param("libelle") String libelleTypeEvaluation);

	@Query("select t from TypeEvaluation t where id_type_evaluation = :statutId and isactive = 1 and is_deleted is null")
	Optional<TypeEvaluation> findById(@Param("statutId") Long fonctionId);
	
	@Query("select t from TypeEvaluation t where lower(libelle_type_evaluation) like lower(:libelle) and isactive= 1 and is_deleted is null")
	Optional<TypeEvaluation> findByLibelleTypeEvaluation(@Param("libelle") String libelleTypeEvaluation);

	@Query("select t from TypeEvaluation t where isactive = 1 and is_deleted is null")
	List<TypeEvaluation> findAll();
}
