package ci.bhci.bhevaluationpro.service;

import java.util.List;

import ci.bhci.bhevaluationpro.domain.Fonction;

/**
 * Service interface for the Fonction entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-04
 */
public interface FonctionService extends AbstractBaseService<Fonction, Long> {

	List<Fonction> findByDepartement(Long departementId);

	List<Fonction> findByDirection(Long directionId);

	boolean existFonction(Long directionId, Long departementId, Long managerIdFonction, String libelleFonction);

}
