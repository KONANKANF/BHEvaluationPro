package ci.bhci.bhevaluationpro.service;

import java.util.List;
import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.domain.dto.FonctionDto;

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

	List<FonctionDto> getAll();

	Optional<Fonction> getById(Long id);

	FonctionDto addEntity(FonctionDto entityDto);

	FonctionDto updateEntity(FonctionDto entityDto, Long id);

	void delete(FonctionDto entityDto, Long id);

}
