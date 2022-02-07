package ci.bhci.bhevaluationpro.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.repository.FonctionRepository;
import ci.bhci.bhevaluationpro.service.FonctionService;
import lombok.extern.log4j.Log4j2;

/**
 * Implementation of Service interface for the Direction entity that extends the
 * AbstractBaseRepositoryImpl implementation
 * 
 * @author kyao
 * @since 2022-01-04
 */

@Service
@Log4j2
@Transactional
public class FonctionServiceImpl extends AbstractBaseRepositoryImpl<Fonction, Long> implements FonctionService {

	private FonctionRepository repository;

	@Autowired
	public FonctionServiceImpl(FonctionRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Transactional
	public Fonction save(Fonction fonction) {
		log.info("-- Begin add new Fonction --");
//		List<Departement> departements = new ArrayList<Departement>();
//		if (departement.getDepartements().size() > 0) {
//			direction.getDepartements().stream().forEach(departement -> {
//				departement.setDirection(direction);
//				departements.add(departement);
//			});
//			departement.setDepartements(departements);
//		}
		Fonction newFonction = this.repository.save(fonction);
		log.info("-- End add new Fonction --");
		return newFonction;
	}

	@Override
	public List<Fonction> findByDirection(Long directionId) {
		return this.repository.findByDirection(directionId);
	}

	@Override
	public List<Fonction> findByDepartement(Long departementId) {
		return this.repository.findByDepartement(departementId);
	}

	@Override
	public boolean existFonction(Long directionId, Long departementId, Long managerIdFonction, String libelleFonction) {
		return this.repository.existFonction(directionId, departementId, managerIdFonction, libelleFonction);
	}

}
