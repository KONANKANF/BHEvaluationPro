package ci.bhci.bhevaluationpro.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.bhci.bhevaluationpro.domain.Periode;
import ci.bhci.bhevaluationpro.repository.PeriodeRepository;
import ci.bhci.bhevaluationpro.service.PeriodeService;

/**
 * Implementation of Service interface for the Statut entity that extends the
 * AbstractBaseRepositoryImpl implementation
 *
 * @author kyao
 * @since 2022-01-20
 */
@Service
public class PeriodeServiceImpl extends AbstractBaseRepositoryImpl<Periode, Long> implements PeriodeService {

	private final PeriodeRepository repository;
	/**
	 * @param abstractBaseRepository
	 */
	@Autowired
	public PeriodeServiceImpl(PeriodeRepository repository) {
		super(repository);
		this.repository = repository;
	}
	@Override
	public boolean existPeriode(String libellePeriode, String annee, Date periodeDebut, Date periodeFin) {
		return this.repository.existPeriode(libellePeriode, annee, periodeDebut, periodeFin);
	}
	@Override
	public Optional<Periode> findByLibellePeriode(String libellePeriode, String annee, Date periodeDebut,
			Date periodeFin) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public boolean existStatut(String libelleStatut) {
//		return this.repository.existStatut(libelleStatut);
//	}
//
//	@Override
//	public Optional<Statut> findByLibelleStatut(String libelleStatut) {
//		return this.repository.findByLibelleStatut(libelleStatut);
//	}
}
