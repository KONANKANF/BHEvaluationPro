package ci.bhci.bhevaluationpro.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.domain.Personnel;
import ci.bhci.bhevaluationpro.domain.PersonnelPoste;
import ci.bhci.bhevaluationpro.repository.FonctionRepository;
import ci.bhci.bhevaluationpro.repository.PersonnelPosteRepository;
import ci.bhci.bhevaluationpro.repository.PersonnelRepository;
import ci.bhci.bhevaluationpro.service.PersonnelService;
import lombok.extern.log4j.Log4j2;

/**
 * Implementation of Service interface for the Personnel entity that extends the
 * AbstractBaseRepositoryImpl implementation
 * 
 * @author kyao
 * @since 2022-01-04
 */

@Service
@Log4j2
@Transactional
public class PersonnelServiceImpl extends AbstractBaseRepositoryImpl<Personnel, Long> implements PersonnelService {

	private PersonnelRepository repository;
	private PersonnelPosteRepository posteRepository;
	private FonctionRepository fonctionRepository;

	@Autowired
	public PersonnelServiceImpl(PersonnelRepository repository, PersonnelPosteRepository posteRepository,
			FonctionRepository fonctionRepository) {
		super(repository);
		this.repository = repository;
		this.posteRepository = posteRepository;
		this.fonctionRepository = fonctionRepository;
	}

	@Transactional
	public Personnel save(Personnel personnel) {
		log.info("-- Begin add new Personnel --");
		List<PersonnelPoste> personnelPostes = new ArrayList<PersonnelPoste>();
		if (personnel.getPersonnelPostes().size() > 0) {
			personnel.getPersonnelPostes().stream().forEach(personnelPoste -> {
				personnelPoste.setPersonnel(personnel);
				personnelPostes.add(personnelPoste);
			});
			personnel.setPersonnelPostes(personnelPostes);
		}
		Personnel newPersonnel = this.repository.save(personnel);
		log.info("-- End add new Categoey --");
		return newPersonnel;
	}

	@Override
	public List<Personnel> findByDirection(Long directionId) {
		List<Personnel> personnelList = new ArrayList<Personnel>();
		List<PersonnelPoste> personnelPosteList = new ArrayList<PersonnelPoste>();
		List<Fonction> fonctions = this.fonctionRepository.findByDirection(directionId);
		if (fonctions.size() > 0) {
			List<PersonnelPoste> personnelPostes = this.posteRepository.findAll();
			fonctions.stream().forEach(fonction -> {
				Predicate<PersonnelPoste> posteIsFound = personnelposte -> personnelposte.getFonction().getId()
						.equals(fonction.getId());
				List<PersonnelPoste> personnelPosteFound = personnelPostes.stream().filter(posteIsFound)
						.collect(Collectors.toList());
				personnelPosteList.addAll(personnelPosteFound);
			});

			List<Personnel> personnels = this.repository.findAll();
			personnelPosteList.stream().forEach(poste -> {
				Predicate<Personnel> personnelIsFound = personnel -> personnel.getId()
						.equals(poste.getPersonnel().getId());
				List<Personnel> personnelFound = personnels.stream().filter(personnelIsFound)
						.collect(Collectors.toList());
				personnelList.addAll(personnelFound);
			});

		}
		return personnelList;
	}

	@Override
	public List<Personnel> findByDepartement(Long departementeId) {
		List<Personnel> personnelList = new ArrayList<Personnel>();
		List<PersonnelPoste> personnelPosteList = new ArrayList<PersonnelPoste>();
		List<Fonction> fonctions = this.fonctionRepository.findByDepartement(departementeId);
		if (fonctions.size() > 0) {
			List<PersonnelPoste> personnelPostes = this.posteRepository.findAll();
			fonctions.stream().forEach(fonction -> {
				Predicate<PersonnelPoste> posteIsFound = personnelposte -> personnelposte.getFonction().getId()
						.equals(fonction.getId());
				List<PersonnelPoste> personnelPosteFound = personnelPostes.stream().filter(posteIsFound)
						.collect(Collectors.toList());
				personnelPosteList.addAll(personnelPosteFound);
			});
			List<Personnel> personnels = this.repository.findAll();
			personnelPosteList.stream().forEach(poste -> {
				Predicate<Personnel> personnelIsFound = personnel -> personnel.getId()
						.equals(poste.getPersonnel().getId());
				List<Personnel> personnelFound = personnels.stream().filter(personnelIsFound)
						.collect(Collectors.toList());
				personnelList.addAll(personnelFound);
			});
		}
		return personnelList;
	}

	@Override
	public List<Personnel> findByFonction(Long fonctionId) {
		List<Personnel> personnelList = new ArrayList<Personnel>();
		List<PersonnelPoste> personnelPostes = this.posteRepository.findByFonction(fonctionId);
		if (personnelPostes.size() > 0) {
			List<Personnel> personnels = this.repository.findAll();
			personnelPostes.stream().forEach(poste -> {
				Predicate<Personnel> personnelIsFound = personnel -> personnel.getId()
						.equals(poste.getPersonnel().getId());
				List<Personnel> personnelFound = personnels.stream().filter(personnelIsFound)
						.collect(Collectors.toList());
				personnelList.addAll(personnelFound);
			});
		}
		return personnelList;
	}

	@Override
	public List<Personnel> findByManager(Long managerIdFonction) {
		List<Personnel> personnelList = new ArrayList<Personnel>();
		List<PersonnelPoste> personnelPosteList = new ArrayList<PersonnelPoste>();
		List<Fonction> fonctions = this.fonctionRepository.findByManager(managerIdFonction);
		if (fonctions.size() > 0) {
			List<PersonnelPoste> personnelPostes = this.posteRepository.findAll();
			fonctions.stream().forEach(fonction -> {
				Predicate<PersonnelPoste> posteIsFound = personnelposte -> personnelposte.getFonction().getId()
						.equals(fonction.getId());
				List<PersonnelPoste> personnelPosteFound = personnelPostes.stream().filter(posteIsFound)
						.collect(Collectors.toList());
				personnelPosteList.addAll(personnelPosteFound);
			});
			List<Personnel> personnels = this.repository.findAll();
			personnelPosteList.stream().forEach(poste -> {
				Predicate<Personnel> personnelIsFound = pers -> pers.getId().equals(poste.getPersonnel().getId());
				List<Personnel> personnelFound = personnels.stream().filter(personnelIsFound)
						.collect(Collectors.toList());
				personnelList.addAll(personnelFound);
			});
		}
		return personnelList;
	}

	@Override
	public List<Personnel> findByPersonnelPoste(Long personnelPosteId) {
		return this.repository.findByPersonnelPostes(personnelPosteId);
	}

	@Override
	public boolean existPersonnel(String matricule, String email) {
		return this.repository.existPersonnelLikeCustomQuery(matricule, email);
	}
}
