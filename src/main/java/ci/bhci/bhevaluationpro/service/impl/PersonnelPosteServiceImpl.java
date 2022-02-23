package ci.bhci.bhevaluationpro.service.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.domain.Personnel;
import ci.bhci.bhevaluationpro.domain.PersonnelPoste;
import ci.bhci.bhevaluationpro.domain.dto.PersonnelPosteDto;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.repository.FonctionRepository;
import ci.bhci.bhevaluationpro.repository.PersonnelPosteRepository;
import ci.bhci.bhevaluationpro.repository.PersonnelRepository;
import ci.bhci.bhevaluationpro.service.PersonnelPosteService;
import ci.bhci.bhevaluationpro.transformer.Transformer;
import lombok.extern.log4j.Log4j2;

/**
 * Implementation for the Personnel entity that extends the
 * AbstractBaseRepositoryImpl implementation
 * 
 * @author kyao
 * @since 2022-01-04
 */

@Service
@Transactional
@Log4j2
public class PersonnelPosteServiceImpl extends AbstractBaseRepositoryImpl<PersonnelPoste, Long>
		implements PersonnelPosteService {

	private final PersonnelPosteRepository repository;
	private final FonctionRepository fonctionRepository;
	private final PersonnelRepository personnelRepository;

	private final Transformer<PersonnelPosteDto, PersonnelPoste> transformer = new Transformer<PersonnelPosteDto, PersonnelPoste>(
			PersonnelPosteDto.class, PersonnelPoste.class);

	@Autowired
	public PersonnelPosteServiceImpl(PersonnelPosteRepository repository, FonctionRepository fonctionRepository, PersonnelRepository personnelRepository) {
		super(repository);
		this.repository = repository;
		this.fonctionRepository = fonctionRepository;
		this.personnelRepository = personnelRepository;
	}

	@Override
	public List<PersonnelPosteDto> findByDirection(Long directionId) throws SQLException {
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
		return this.transformer.convertToDto(personnelPosteList);
	}

	@Override
	public List<PersonnelPosteDto> findByDepartement(Long departementeId) throws SQLException {
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
		return this.transformer.convertToDto(personnelPosteList);
	}

	@Override
	public List<PersonnelPosteDto> findByPersonnel(Long personnelId) throws SQLException {
		return this.transformer.convertToDto(this.repository.findByPersonnel(personnelId));
	}

	@Override
	public boolean existPersonnelPoste(Long personnelId, Long fonctionId) throws SQLException {
		return this.repository.existPersonnelPoste(personnelId, fonctionId);
	}

	@Override
	public List<PersonnelPosteDto> getAll() throws SQLException {
		log.info("-- Get all entities PersonnelPoste : Begin --");
		try {
			log.info("-- All entities PersonnelPoste get successfully --");
			return this.transformer.convertToDto(this.repository.findAll());
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}
	
	@Override
	public Optional<PersonnelPoste> findById(Long id) throws SQLException {
		log.info("-- Find entity PersonnelPoste by Id : Begin --");
		try {
			log.info("-- Entity PersonnelPoste Id : " + id + " found successfully --");
			return this.repository.findById(id);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public List<PersonnelPosteDto> findByPesonnelAndFonction(Long idPersonnel, Long idFonction) throws SQLException {
		log.info("-- Get all entities PersonnelPoste : Begin --");
		try {
			log.info("-- All entities PersonnelPoste get successfully --");
			return this.transformer.convertToDto(this.repository.findByPersonnelAndFonction(idPersonnel, idFonction));
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public PersonnelPosteDto addEntity(PersonnelPosteDto entityDto) throws SQLException {
		log.info("-- Add entity PersonnelPoste : Begin --");
		try {
			PersonnelPoste entity = this.transformer.convertToEntity(entityDto);
			Optional<Fonction> fonctionEntity = this.fonctionRepository.findById(entityDto.getIdFonction());
			Optional<Personnel> personnelEntity = this.personnelRepository.findById(entityDto.getIdFonction());
			entity.setFonction(fonctionEntity.get());
			entity.setPersonnel(personnelEntity.get());
			PersonnelPoste newEntity = this.repository.save(entity);
			log.info("-- Add entity PersonnelPoste : End successfully --");
			return this.transformer.convertToDto(newEntity);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public PersonnelPosteDto updateEntity(PersonnelPosteDto entityDto, Long id) throws SQLException {
		log.info("-- Update entity PersonnelPoste : Begin --");
		try {
			PersonnelPoste entity = this.repository.findById(id).orElse(null);
			Optional<Fonction> fonctionEntity = this.fonctionRepository.findById(entityDto.getIdFonction());
			Optional<Personnel> personnelEntity = this.personnelRepository.findById(entityDto.getIdFonction());
			entity.setModifiedAt(LocalDateTime.now());
			entity.setModifiedBy(entityDto.getModifiedBy());
			entity.setDebutPoste(entityDto.getDebutPoste());
			entity.setFinPoste(entityDto.getFinPoste());
			entity.setFonction(fonctionEntity.get());
			entity.setPersonnel(personnelEntity.get());
			entity.setIsActive(entityDto.getIsActive());
			PersonnelPoste newEntity = this.repository.save(entity);
			log.info("-- Update entity PersonnelPoste : End successfully --");
			return this.transformer.convertToDto(newEntity);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}
	
	@Override
	public void deleteEntity(PersonnelPosteDto entityDto) throws SQLException {
		this.repository.save(this.transformer.convertToEntity(entityDto));
		
		log.info("-- Delete entity PersonnelPoste : Begin --");
		try {
			PersonnelPoste entity = this.repository.findById(entityDto.getId()).orElse(null);
			entity.setDeletedAt(LocalDateTime.now());
			entity.setDeletedBy(entityDto.getModifiedBy());
			entity.setIsActive(entityDto.getIsActive());
			this.repository.save(entity);
			log.info("-- Delete entity PersonnelPoste : End successfully --");
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}
}
