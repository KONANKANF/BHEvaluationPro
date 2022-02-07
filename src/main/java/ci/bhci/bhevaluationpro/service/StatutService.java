package ci.bhci.bhevaluationpro.service;

import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.Statut;

/**
 * StatutService
 *
 * @author kyao
 * @since 2022-01-20
 */
public interface StatutService extends AbstractBaseService<Statut, Long> {

	/**
	 * @param libelleStatut
	 * @return
	 */
	boolean existStatut(String libelleStatut);

	/**
	 * @param libelleStatut
	 * @return
	 */
	Optional<Statut> findByLibelleStatut(String libelleStatut);

}
