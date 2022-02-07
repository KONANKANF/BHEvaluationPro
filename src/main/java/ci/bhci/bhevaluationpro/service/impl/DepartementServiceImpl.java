package ci.bhci.bhevaluationpro.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.repository.DepartementRepository;
import ci.bhci.bhevaluationpro.service.DepartementService;
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
public class DepartementServiceImpl extends AbstractBaseRepositoryImpl<Departement, Long> implements DepartementService {

	private DepartementRepository repository;

	@Autowired
	public DepartementServiceImpl(DepartementRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Transactional
	public Departement save(Departement departement) {
		log.info("-- Begin add new Departement --");
		Departement newDepartement = this.repository.save(departement);
		log.info("-- End add new Departement --");
		return newDepartement;
	}


	@Override
	public List<Departement> findByDirection(Long directionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existDepartement(Long directionId, String libelleDepartement) {		
		return this.repository.existDepartement(directionId, libelleDepartement);
	}

}
