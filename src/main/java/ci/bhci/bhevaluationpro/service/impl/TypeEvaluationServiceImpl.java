package ci.bhci.bhevaluationpro.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.bhci.bhevaluationpro.domain.TypeEvaluation;
import ci.bhci.bhevaluationpro.repository.TypeEvaluationRepository;
import ci.bhci.bhevaluationpro.service.TypeEvaluationService;

/**
 * Implementation of Service interface for the TypeEvaluation entity that extends the
 * AbstractBaseRepositoryImpl implementation
 *
 * @author kyao
 * @since 2022-01-20
 */
@Service
public class TypeEvaluationServiceImpl extends AbstractBaseRepositoryImpl<TypeEvaluation, Long> implements TypeEvaluationService {

	private final TypeEvaluationRepository repository;
	/**
	 * @param abstractBaseRepository
	 */
	@Autowired
	public TypeEvaluationServiceImpl(TypeEvaluationRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public boolean existTypeEvaluation(String libelleTypeEvaluation) {
		return this.repository.existTypeEvaluation(libelleTypeEvaluation);
	}

	@Override
	public Optional<TypeEvaluation> findByLibelleTypeEvaluation(String libelleTypeEvaluation) {
		return this.repository.findByLibelleTypeEvaluation(libelleTypeEvaluation);
	}
}
