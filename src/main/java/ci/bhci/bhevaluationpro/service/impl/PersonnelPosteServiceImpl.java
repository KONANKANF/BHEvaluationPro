package ci.bhci.bhevaluationpro.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.domain.PersonnelPoste;
import ci.bhci.bhevaluationpro.repository.FonctionRepository;
import ci.bhci.bhevaluationpro.repository.PersonnelPosteRepository;
import ci.bhci.bhevaluationpro.service.PersonnelPosteService;

/**
 * Implementation for the Personnel entity that extends the
 * AbstractBaseRepositoryImpl implementation
 * 
 * @author kyao
 * @since 2022-01-04
 */

@Service
@Transactional
public class PersonnelPosteServiceImpl extends AbstractBaseRepositoryImpl<PersonnelPoste, Long>
		implements PersonnelPosteService {

	private PersonnelPosteRepository repository;
	private FonctionRepository fonctionRepository;

	@Autowired
	public PersonnelPosteServiceImpl(PersonnelPosteRepository repository, FonctionRepository fonctionRepository) {
		super(repository);
		this.repository = repository;
		this.fonctionRepository = fonctionRepository;
	}

	@Override
	public List<PersonnelPoste> findByDirection(Long directionId) {
		List<PersonnelPoste> personnelPosteList = new ArrayList<>();
		List<Fonction> fonctions = this.fonctionRepository.findByDirection(directionId);
		if (fonctions.size() > 0) {

			List<PersonnelPoste> personnelPostes = this.repository.findAll();
			fonctions.stream().forEach(fonction -> {
				Predicate<PersonnelPoste> posteIsFound = personnelposte -> personnelposte.getFonction().getId()
						.equals(fonction.getId());
				List<PersonnelPoste> personnelPosteFound = personnelPostes.stream().filter(posteIsFound)
						.collect(Collectors.toList());
				personnelPosteList.addAll(personnelPosteFound);
			});
		}
		return personnelPosteList;
	}

	@Override
	public List<PersonnelPoste> findByDepartement(Long departementeId) {
		List<PersonnelPoste> personnelPosteList = new ArrayList<PersonnelPoste>();
		List<Fonction> fonctions = this.fonctionRepository.findByDepartement(departementeId);
		if (fonctions.size() > 0) {
			List<PersonnelPoste> personnelPostes = this.repository.findAll();
			fonctions.stream().forEach(fonction -> {
				Predicate<PersonnelPoste> posteIsFound = personnelposte -> personnelposte.getFonction().getId()
						.equals(fonction.getId());
				List<PersonnelPoste> personnelPosteFound = personnelPostes.stream().filter(posteIsFound)
						.collect(Collectors.toList());
				personnelPosteList.addAll(personnelPosteFound);
			});
		}
		return personnelPosteList;
	}

	@Override
	public void delete(PersonnelPoste personnelPoste) {
		this.repository.save(personnelPoste);
	}

	@Override
	public List<PersonnelPoste> findByPersonnel(Long personnelId) {
		return this.repository.findByPersonnel(personnelId);
	}

	@Override
	public boolean existPersonnelPoste(Long personnelId, Long fonctionId) {
		return this.repository.existPersonnelPoste(personnelId, fonctionId);
	}
}
