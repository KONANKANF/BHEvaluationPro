package ci.bhci.bhevaluationpro.service;

import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.TypeEvaluation;

/**
 * TypeEvaluationService
 *
 * @author kyao
 * @since 2022-01-20
 */
public interface TypeEvaluationService extends AbstractBaseService<TypeEvaluation, Long> {

	/**
	 * @param libelleTypeEvaluation
	 * @return
	 */
	boolean existTypeEvaluation(String libelleTypeEvaluation);

	/**
	 * @param libelleTypeEvaluation
	 * @return
	 */
	Optional<TypeEvaluation> findByLibelleTypeEvaluation(String libelleTypeEvaluation);

}
