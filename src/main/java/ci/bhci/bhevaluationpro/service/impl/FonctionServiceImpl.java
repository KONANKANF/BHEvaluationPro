package ci.bhci.bhevaluationpro.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.Direction;
import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.domain.Personnel;
import ci.bhci.bhevaluationpro.domain.PersonnelPoste;
import ci.bhci.bhevaluationpro.domain.dto.FonctionDto;
import ci.bhci.bhevaluationpro.domain.dto.PersonnelPosteDto;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.repository.DepartementRepository;
import ci.bhci.bhevaluationpro.repository.DirectionRepository;
import ci.bhci.bhevaluationpro.repository.FonctionRepository;
import ci.bhci.bhevaluationpro.repository.PersonnelRepository;
import ci.bhci.bhevaluationpro.service.FonctionService;
import ci.bhci.bhevaluationpro.transformer.Transformer;
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

	private final FonctionRepository repository;
	private final DirectionRepository directionRepository;
	private final DepartementRepository departementRepository;
	private final PersonnelRepository personnelRepository;

	private final Transformer<FonctionDto, Fonction> transformer = new Transformer<FonctionDto, Fonction>(
			FonctionDto.class, Fonction.class);

	private final Transformer<PersonnelPosteDto, PersonnelPoste> personnelPosteTransformer = new Transformer<PersonnelPosteDto, PersonnelPoste>(
			PersonnelPosteDto.class, PersonnelPoste.class);

	@Autowired
	public FonctionServiceImpl(FonctionRepository repository, DirectionRepository directionRepository,
			PersonnelRepository personnelRepository, DepartementRepository departementRepository) {
		super(repository);
		this.repository = repository;
		this.directionRepository = directionRepository;
		this.departementRepository = departementRepository;
		this.personnelRepository = personnelRepository;
	}

//	@Transactional
//	public Fonction save(Fonction fonction) {
//		log.info("-- Begin add new Fonction --");
////		List<Departement> departements = new ArrayList<Departement>();
////		if (departement.getDepartements().size() > 0) {
////			direction.getDepartements().stream().forEach(departement -> {
////				departement.setDirection(direction);
////				departements.add(departement);
////			});
////			departement.setDepartements(departements);
////		}
//		Fonction newFonction = this.repository.save(fonction);
//		log.info("-- End add new Fonction --");
//		return newFonction;
//	}

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

	@Override
	public List<FonctionDto> getAll() {
		log.info("-- Get all entities Departement : Begin --");
		try {
			log.info("-- All entities Departement get successfully --");
			return this.transformer.convertToDto(this.repository.findAll());
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public Optional<Fonction> getById(Long id) {
		log.info("-- Find entity Fonction by Id : Begin --");
		try {
			log.info("-- Entity Fonction Id : " + id + " found successfully --");
			return this.repository.findById(id);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public FonctionDto addEntity(FonctionDto entityDto) {
		log.info("-- Add entity Fonction : Begin --");
		try {
			List<PersonnelPoste> children = new ArrayList<PersonnelPoste>();
			Direction parent = this.directionRepository.getById(entityDto.getDirectionId());
			Departement parentDep = this.departementRepository.getById(entityDto.getDepartementId());
			Fonction entity = this.transformer.convertToEntity(entityDto);
			if (entityDto.getPersonnelPosteDto().size() > 0) {
				entityDto.getPersonnelPosteDto().stream().forEach(element -> {
					Personnel personnelEntity = this.personnelRepository.getById(element.getPersonnelId());
					PersonnelPoste childEntity = this.personnelPosteTransformer.convertToEntity(element);
					childEntity.setPersonnel(personnelEntity);
					childEntity.setFonction(entity);
					children.add(childEntity);
				});
				entity.setPersonnelPostes(children);
				log.info("-- Entities PersonnelPoste added --");
			}
			entity.setDirection(parent);
			entity.setDepartement(parentDep);
			Fonction newEntity = this.repository.save(entity);
			log.info("-- Add entity Fonction : End successfully --");
			return this.transformer.convertToDto(newEntity);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public FonctionDto updateEntity(FonctionDto entityDto, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void delete(FonctionDto entityDto, Long id) {
		// TODO Auto-generated method stub

	}

}
