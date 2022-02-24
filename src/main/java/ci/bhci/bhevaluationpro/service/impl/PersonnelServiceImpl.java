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
import ci.bhci.bhevaluationpro.domain.dto.PersonnelDto;
import ci.bhci.bhevaluationpro.domain.dto.PersonnelPosteDto;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.repository.FonctionRepository;
import ci.bhci.bhevaluationpro.repository.PersonnelPosteRepository;
import ci.bhci.bhevaluationpro.repository.PersonnelRepository;
import ci.bhci.bhevaluationpro.service.PersonnelService;
import ci.bhci.bhevaluationpro.transformer.Transformer;
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
	private PersonnelPosteRepository personnelPosteRepository;
	private FonctionRepository fonctionRepository;

	private final Transformer<PersonnelDto, Personnel> transformer = new Transformer<PersonnelDto, Personnel>(
			PersonnelDto.class, Personnel.class);
	
	private final Transformer<PersonnelPosteDto, PersonnelPoste> transformerPersonnelPoste = new Transformer<PersonnelPosteDto, PersonnelPoste>(
			PersonnelPosteDto.class, PersonnelPoste.class);

	@Autowired
	public PersonnelServiceImpl(PersonnelRepository repository, PersonnelPosteRepository personnelPosteRepository,
			FonctionRepository fonctionRepository) {
		super(repository);
		this.repository = repository;
		this.personnelPosteRepository = personnelPosteRepository;
		this.fonctionRepository = fonctionRepository;
	}

	@Override
	public List<PersonnelDto> getAll() throws SQLException {
		log.info("-- Get all entities Personnel : Begin --");
		try {
			List<PersonnelDto> personnelList = this.transformer.convertToDto(this.repository.findAll());
			log.info("-- All entities Personnel get successfully --");
			return this.setPersonnelPoste(personnelList);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public Optional<Personnel> getById(Long id) throws SQLException {
		log.info("-- Find entity Personnel by Id : Begin --");
		try {
			log.info("-- Entity Personnel Id : " + id + " found successfully --");
			return this.repository.findById(id);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}
	
	@Override
	public List<PersonnelDto> findByDirection(Long directionId) throws SQLException {
		List<PersonnelDto> personnelList = new ArrayList<PersonnelDto>();
		List<PersonnelPoste> personnelPosteList = new ArrayList<PersonnelPoste>();
		List<Fonction> fonctions = this.fonctionRepository.findByDirection(directionId);
		if (fonctions.size() > 0) {
			List<PersonnelPoste> personnelPostes = this.personnelPosteRepository.findAll();
			
			fonctions.stream().forEach(fonction -> {
				Predicate<PersonnelPoste> posteIsFound = personnelposte -> personnelposte.getFonction().getId()
						.equals(fonction.getId());
				List<PersonnelPoste> personnelPosteFound = personnelPostes.stream().filter(posteIsFound)
						.collect(Collectors.toList());
				personnelPosteList.addAll(personnelPosteFound);
			});			
			List<PersonnelDto> personnels = this.transformer.convertToDto(this.repository.findAll());
			personnelPosteList.stream().forEach(poste -> {
				Predicate<PersonnelDto> personnelIsFound = personnel -> personnel.getId()
						.equals(poste.getPersonnel().getId());
				List<PersonnelDto> personnelFound = personnels.stream().filter(personnelIsFound)
						.collect(Collectors.toList());
				personnelList.addAll(personnelFound);
			});
		}
		return this.setPersonnelPoste(personnelList);
	}

	@Override
	public List<PersonnelDto> findByDepartement(Long departementeId) throws SQLException {
		List<PersonnelDto> personnelList = new ArrayList<PersonnelDto>();
		List<PersonnelPoste> personnelPosteList = new ArrayList<PersonnelPoste>();
		List<Fonction> fonctions = this.fonctionRepository.findByDepartement(departementeId);
		if (fonctions.size() > 0) {
			List<PersonnelPoste> personnelPostes = this.personnelPosteRepository.findAll();
			fonctions.stream().forEach(fonction -> {
				Predicate<PersonnelPoste> posteIsFound = personnelposte -> personnelposte.getFonction().getId()
						.equals(fonction.getId());
				List<PersonnelPoste> personnelPosteFound = personnelPostes.stream().filter(posteIsFound)
						.collect(Collectors.toList());
				personnelPosteList.addAll(personnelPosteFound);
			});
			List<PersonnelDto> personnels = this.transformer.convertToDto(this.repository.findAll());
			personnelPosteList.stream().forEach(poste -> {
				Predicate<PersonnelDto> personnelIsFound = personnel -> personnel.getId()
						.equals(poste.getPersonnel().getId());
				List<PersonnelDto> personnelFound = personnels.stream().filter(personnelIsFound)
						.collect(Collectors.toList());
				personnelList.addAll(personnelFound);
			});
		}
		return this.setPersonnelPoste(personnelList);
	}

	@Override
	public List<PersonnelDto> findByFonction(Long fonctionId) throws SQLException {
		List<PersonnelDto> personnelList = new ArrayList<PersonnelDto>();
		List<PersonnelPoste> personnelPostes = this.personnelPosteRepository.findByFonction(fonctionId);
		if (personnelPostes.size() > 0) {
			List<PersonnelDto> personnels = this.transformer.convertToDto(this.repository.findAll());
			personnelPostes.stream().forEach(poste -> {
				Predicate<PersonnelDto> personnelIsFound = personnel -> personnel.getId()
						.equals(poste.getPersonnel().getId());
				List<PersonnelDto> personnelFound = personnels.stream().filter(personnelIsFound)
						.collect(Collectors.toList());
				personnelList.addAll(personnelFound);
			});
		}
		return this.setPersonnelPoste(personnelList);
	}

	@Override
	public List<PersonnelDto> findByManager(Long managerIdFonction) throws SQLException {
		List<PersonnelDto> personnelList = new ArrayList<PersonnelDto>();
		List<PersonnelPoste> personnelPosteList = new ArrayList<PersonnelPoste>();
		List<Fonction> fonctions = this.fonctionRepository.findByManager(managerIdFonction);
		if (fonctions.size() > 0) {
			List<PersonnelPoste> personnelPostes = this.personnelPosteRepository.findAll();
			fonctions.stream().forEach(fonction -> {
				Predicate<PersonnelPoste> posteIsFound = personnelposte -> personnelposte.getFonction().getId()
						.equals(fonction.getId());
				List<PersonnelPoste> personnelPosteFound = personnelPostes.stream().filter(posteIsFound)
						.collect(Collectors.toList());
				personnelPosteList.addAll(personnelPosteFound);
			});
			List<PersonnelDto> personnels = this.transformer.convertToDto(this.repository.findAll());
			personnelPosteList.stream().forEach(poste -> {
				Predicate<PersonnelDto> personnelIsFound = pers -> pers.getId().equals(poste.getPersonnel().getId());
				List<PersonnelDto> personnelFound = personnels.stream().filter(personnelIsFound)
						.collect(Collectors.toList());
				personnelList.addAll(personnelFound);
			});
		}
		return this.setPersonnelPoste(personnelList);
	}

	@Override
	public PersonnelDto findByPersonnelPoste(Long personnelPosteId) throws SQLException {
		Optional<PersonnelPoste> personnelPoste = this.personnelPosteRepository.findById(personnelPosteId);
		PersonnelDto personnelDto = new PersonnelDto();
		if (personnelPoste.isPresent()) {
			personnelDto = this.transformer.convertToDto(personnelPoste.get().getPersonnel());
			List<PersonnelPoste> personnelPostes = this.personnelPosteRepository.findByPersonnel(personnelDto.getId());
			personnelDto.setPersonnelPosteDto(this.transformerPersonnelPoste.convertToDto(personnelPostes));
		}
		return personnelDto;
	}
	
	private List<PersonnelDto> setPersonnelPoste(List<PersonnelDto> personnelDtos){
		List<PersonnelDto> personnels = new ArrayList<PersonnelDto>();
		personnelDtos.stream().forEach(personnel ->{
			List<PersonnelPoste> personnelPostes = this.personnelPosteRepository.findByPersonnel(personnel.getId());
			personnel.setPersonnelPosteDto(this.transformerPersonnelPoste.convertToDto(personnelPostes));
			personnels.add(personnel);
		});
		return personnels;
	}

	@Override
	public boolean existPersonnel(String matricule, String email) throws SQLException {
		return this.repository.existPersonnelLikeCustomQuery(matricule, email);
	}

	@Override
	public PersonnelDto addEntity(PersonnelDto entityDto) throws SQLException {
		log.info("-- Add entity Personnel : Begin --");
		try {
			Personnel entity = this.transformer.convertToEntity(entityDto);
			if (entityDto.getPersonnelPosteDto().size() > 0) {
				entityDto.getPersonnelPosteDto().stream().forEach(element -> {
					PersonnelPoste childEntity = new PersonnelPoste();
					Optional<Fonction> fonctionEntity = this.fonctionRepository.findById(element.getIdFonction());
					childEntity.setCreatedAt(LocalDateTime.now());
					childEntity.setCreatedBy(entityDto.getCreatedBy());
					childEntity.setDebutPoste(element.getDebutPoste());
					childEntity.setFinPoste(element.getFinPoste());
					childEntity.setIsActive(element.getIsActive());
					childEntity.setPersonnel(entity);
					if (fonctionEntity.isPresent()) {
						childEntity.setFonction(fonctionEntity.get());
					}
					entity.addPersonnel(childEntity);
				});
				log.info("-- Entities PersonnelPoste added --");
			}
			Personnel newEntity = this.repository.save(entity);
			log.info("-- Add entity Personnel : End successfully --");
			return this.transformer.convertToDto(newEntity);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public PersonnelDto updateEntity(PersonnelDto entityDto, Long id) throws SQLException {
		log.info("-- Update entity Personnel : Begin --");
		try {
			Personnel entity = this.repository.findById(id).orElse(null);
			if (entityDto.getPersonnelPosteDto().size() > 0) {
				entityDto.getPersonnelPosteDto().stream().forEach(element -> {
					Optional<Fonction> fonctionEntity = this.fonctionRepository.findById(element.getIdPersonnel());
					if (element.getId() == null) {
						PersonnelPoste childEntity = new PersonnelPoste();
						childEntity.setCreatedAt(LocalDateTime.now());
						childEntity.setCreatedBy(entityDto.getModifiedBy());
						childEntity.setDebutPoste(element.getDebutPoste());
						childEntity.setFinPoste(element.getFinPoste());
						childEntity.setIsActive(element.getIsActive());
						childEntity.setPersonnel(entity);
						if (fonctionEntity.isPresent()) {
							childEntity.setFonction(fonctionEntity.get());
						}
						entity.addPersonnel(childEntity);
						log.info("-- New entity PersonnelPoste added --");
					} else {
						PersonnelPoste childEntity = this.personnelPosteRepository.getById(element.getId());
						childEntity.setModifiedAt(LocalDateTime.now());
						childEntity.setModifiedBy(entityDto.getModifiedBy());
						childEntity.setDebutPoste(element.getDebutPoste());
						childEntity.setFinPoste(element.getFinPoste());
						childEntity.setIsActive(element.getIsActive());
						childEntity.setPersonnel(entity);
						if (fonctionEntity.isPresent()) {
							childEntity.setFonction(fonctionEntity.get());
						}
						log.info("-- Entity PersonnelPoste updated --");
					}
				});
			}
			entity.setModifiedAt(LocalDateTime.now());
			entity.setModifiedBy(entityDto.getModifiedBy());
			entity.setIsActive(entityDto.getIsActive());
			Personnel editedEntity = this.repository.save(entity);
			log.info("-- Update entity Personnel : End successfully --");
			return this.transformer.convertToDto(editedEntity);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public void deleteEntity(PersonnelDto entityDto) throws SQLException {
		log.info("-- Delete entity Fonction : Begin --");
		try {
			Personnel entity = this.findById(entityDto.getId()).orElse(null);
			if (entity != null) {
				entity.getPersonnelPostes().stream().forEach(element -> {
					PersonnelPoste childEntity = this.personnelPosteRepository.findById(element.getId()).orElse(null);
					if(childEntity != null) {
					childEntity.setIsDeleted(true);
					childEntity.setDeletedAt(LocalDateTime.now());
					childEntity.setDeletedBy(entityDto.getDeletedBy());
					childEntity.setIsActive(false);
					log.info("-- Entity Fonction deleted --");
					}					
				});
				entity.setIsDeleted(true);
				entity.setDeletedAt(LocalDateTime.now());
				entity.setDeletedBy(entityDto.getDeletedBy());
				entity.setIsActive(false);
				this.repository.save(entity);
				log.info("-- Delete entity Fonction : End successfully --");
			}
		} catch (SQLException e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}
}
