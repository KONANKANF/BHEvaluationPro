package ci.bhci.bhevaluationpro.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.bhci.bhevaluationpro.domain.Statut;
import ci.bhci.bhevaluationpro.repository.StatutRepository;
import ci.bhci.bhevaluationpro.service.StatutService;

/**
 * Implementation of Service interface for the Statut entity that extends the
 * AbstractBaseRepositoryImpl implementation
 *
 * @author kyao
 * @since 2022-01-20
 */
@Service
public class StatutServiceImpl extends AbstractBaseRepositoryImpl<Statut, Long> implements StatutService {

	private final StatutRepository repository;
	/**
	 * @param abstractBaseRepository
	 */
	@Autowired
	public StatutServiceImpl(StatutRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public boolean existStatut(String libelleStatut) {
		return this.repository.existStatut(libelleStatut);
	}

	@Override
	public Optional<Statut> findByLibelleStatut(String libelleStatut) {
		return this.repository.findByLibelleStatut(libelleStatut);
	}
}
