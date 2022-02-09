package ci.bhci.bhevaluationpro.service.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.Direction;
import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.domain.dto.DepartementDto;
import ci.bhci.bhevaluationpro.domain.dto.DirectionDto;
import ci.bhci.bhevaluationpro.domain.dto.FonctionDto;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.repository.DepartementRepository;
import ci.bhci.bhevaluationpro.repository.FonctionRepository;
import ci.bhci.bhevaluationpro.service.DepartementService;
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
public class DepartementServiceImpl extends AbstractBaseRepositoryImpl<Departement, Long> implements DepartementService {

	private final DepartementRepository repository;
	private final FonctionRepository fonctionRepository;
	
	private final Transformer<FonctionDto, Fonction> fonctionTransformer = new Transformer<FonctionDto, Fonction>(
			FonctionDto.class, Fonction.class);

	private final Transformer<DepartementDto, Departement> transformer = new Transformer<DepartementDto, Departement>(
			DepartementDto.class, Departement.class);

	@Autowired
	public DepartementServiceImpl(DepartementRepository repository, FonctionRepository fonctionRepository) {
		super(repository);
		this.repository = repository;
		this.fonctionRepository = fonctionRepository;
	}

	@Override
	public boolean existDepartement(Long directionId, String libelleDepartement) {
		try {
			return this.repository.existDepartement(directionId, libelleDepartement);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}		
	}

	@Override
	public List<DepartementDto> getAll() {
		log.info("-- Get all entities Direction : Begin --");
		try {
			log.info("-- All entities Direction get successfully --");
			return this.transformer.convertToDto(this.repository.findAll());
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public Optional<DepartementDto> getById(Long id) {
		log.info("-- Find entity Direction by Id : Begin --");
		try {
			log.info("-- Entity Direction Id : " + id + " found successfully --");
			return Optional.of(this.transformer.convertToDto(this.repository.findById(id)));
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public DepartementDto addEntity(DepartementDto entityDto) {
		log.info("-- Add entity Departement : Begin --");
		try {
			List<Fonction> children = new ArrayList<Fonction>();
			Departement entity = this.transformer.convertToEntity(entityDto);
			if (entityDto.getFonctionDto().size() > 0) {
				entityDto.getFonctionDto().stream().forEach(departementDto -> {
					Fonction childEntity = this.fonctionTransformer.convertToEntity(departementDto);
					childEntity.setDepartement(entity);
					children.add(childEntity);
				});
				entity.setModifiedAt(LocalDateTime.now());
				entity.setFonctions(children);
				log.info("-- Entity(ies) Fonction added --");
			}
			Departement newEntity = this.repository.save(entity);
			log.info("-- Add entity Departement : End successfully --");
			return this.transformer.convertToDto(newEntity);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public DepartementDto updateEntity(DepartementDto entityDto, Long id) {
		log.info("-- Update entity Departement : Begin --");
		try {
			Departement entity = this.findById(entityDto.getId()).orElse(null);
			if (entityDto.getFonctionDto().size() > 0) {
				entityDto.getFonctionDto().stream().forEach(element -> {
					if (!this.fonctionRepository.existFonction(entityDto.getDirectionId(),id, null, element.getLibelleFonction())) {
						Fonction childEntity = new Fonction();
						childEntity.setCreatedAt(LocalDateTime.now());
						childEntity.setCreatedBy(entityDto.getModifiedBy());
						childEntity.setLibelleFonction(element.getLibelleFonction());
						childEntity.setIsActive(element.getIsActive());
						childEntity.setDepartement(entity);
						entity.addFonction(childEntity);
						log.info("-- New entity Fonction added --");
					} else {
						if (this.fonctionRepository.findById(element.getId()).isPresent()) {
							Fonction childEntity = this.fonctionRepository.findById(element.getId()).get();
							childEntity.setModifiedAt(LocalDateTime.now());
							childEntity.setModifiedBy(entityDto.getModifiedBy());
							childEntity.setLibelleFonction(element.getLibelleFonction());
							childEntity.setIsActive(element.getIsActive());
							childEntity.setDepartement(entity);
							log.info("-- Entity Fonction updated --");
						}
					}
				});
			}
			entity.setModifiedBy(entityDto.getModifiedBy());
			entity.setModifiedAt(LocalDateTime.now());
			entity.setLibelleDepartement(entityDto.getLibelleDepartement());
			entity.setIsActive(entityDto.getIsActive());
			Departement editedEntity = this.repository.save(entity);
			log.info("-- Update entity Departement : End successfully --");
			return this.transformer.convertToDto(editedEntity);
		} catch (SQLException e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void delete(DepartementDto entityDto, Long id) {
		log.info("-- Delete entity Departement : Begin --");
		try {
			Departement entity = this.findById(entityDto.getId()).orElse(null);
			if (entity != null) {
				entity.getFonctions().stream().forEach(element -> {
					Fonction childEntity = this.fonctionRepository.findById(element.getId()).get();
					childEntity.setIsDeleted(true);
					childEntity.setDeletedAt(LocalDateTime.now());
					childEntity.setDeletedBy(entityDto.getDeletedBy());
					childEntity.setIsActive(false);
					log.info("-- Entity Fonction deleted --");
				});
				entity.setIsDeleted(true);
				entity.setDeletedAt(LocalDateTime.now());
				entity.setDeletedBy(entityDto.getDeletedBy());
				entity.setIsActive(false);
				this.repository.save(entity);
				log.info("-- Delete entity Departement : End successfully --");
			}
		}catch(SQLException e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}
}
