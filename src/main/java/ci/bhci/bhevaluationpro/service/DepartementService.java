package ci.bhci.bhevaluationpro.service;

import java.util.List;

import ci.bhci.bhevaluationpro.domain.Departement;

/**
 * Service interface for the Departement entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-04
 */
public interface DepartementService extends AbstractBaseService<Departement, Long> {

	List<Departement> findByDirection(Long directionId);
	
	boolean existDepartement(Long directionId, String libelleDepartement); 
}
