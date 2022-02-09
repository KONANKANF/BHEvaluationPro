package ci.bhci.bhevaluationpro.service;

import java.util.List;
import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.dto.DepartementDto;

/**
 * Service interface for the Departement entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-08
 */
public interface DepartementService extends AbstractBaseService<Departement, Long> {

//	List<Departement> findByDirection(Long directionId);
	
	boolean existDepartement(Long directionId, String libelleDepartement);

	List<DepartementDto> getAll();

	Optional<DepartementDto> getById(Long id);

	DepartementDto addEntity(DepartementDto entityDto);

	DepartementDto updateEntity(DepartementDto entityDto, Long id);

	void delete(DepartementDto entityDto, Long id); 
}
