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
import ci.bhci.bhevaluationpro.domain.dto.FonctionDto;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.repository.DepartementRepository;
import ci.bhci.bhevaluationpro.repository.DirectionRepository;
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
public class DepartementServiceImpl extends AbstractBaseRepositoryImpl<Departement, Long>
		implements DepartementService {

	private final DepartementRepository repository;
	private final DirectionRepository directionRepository;
	private final FonctionRepository fonctionRepository;

	private final Transformer<FonctionDto, Fonction> fonctionTransformer = new Transformer<FonctionDto, Fonction>(
			FonctionDto.class, Fonction.class);

	private final Transformer<DepartementDto, Departement> transformer = new Transformer<DepartementDto, Departement>(
			DepartementDto.class, Departement.class);

	@Autowired
	public DepartementServiceImpl(DepartementRepository repository, FonctionRepository fonctionRepository,
			DirectionRepository directionRepository) {
		super(repository);
		this.repository = repository;
		this.directionRepository = directionRepository;
		this.fonctionRepository = fonctionRepository;
	}

	@Override
	public boolean existDepartement(Long directionId, String libelleDepartement) throws SQLException {
		try {
			return this.repository.existDepartement(directionId, libelleDepartement);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public List<DepartementDto> getAll() throws SQLException {
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
	public Optional<Departement> getById(Long id) throws SQLException {
		log.info("-- Find entity Departement by Id : Begin --");
		try {
			log.info("-- Entity Departement Id : " + id + " found successfully --");
			return this.repository.findById(id);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public DepartementDto addEntity(DepartementDto entityDto) throws SQLException {
		log.info("-- Add entity Departement : Begin --");
		try {
			List<Fonction> children = new ArrayList<Fonction>();
			Direction parent = this.directionRepository.getById(entityDto.getIdDirection());
			Departement entity = this.transformer.convertToEntity(entityDto);
			if (entityDto.getFonctionDto().size() > 0) {
				entityDto.getFonctionDto().stream().forEach(element -> {
					Optional<Fonction> managerEntity = this.fonctionRepository.findById(element.getManagerIdFonction());
					Fonction childEntity = this.fonctionTransformer.convertToEntity(element);
					childEntity.setDepartement(entity);
					childEntity.setDirection(parent);
					if (managerEntity.isPresent()) {
						childEntity.setManagerIdFonction(managerEntity.get());
					}
					children.add(childEntity);
				});
				entity.setFonctions(children);
				log.info("-- Entities Fonction added --");
			}
			entity.setDirection(parent);
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
	public DepartementDto updateEntity(DepartementDto entityDto, Long id) throws SQLException {
		log.info("-- Update entity Departement : Begin --");
		try {
			Direction parent = this.directionRepository.findById(entityDto.getIdDirection()).get();
			Departement entity = this.findById(entityDto.getId()).orElse(null);
			if (entityDto.getFonctionDto().size() > 0) {
				entityDto.getFonctionDto().stream().forEach(element -> {
					if (!this.fonctionRepository.findById(element.getId()).isPresent()) {
						Optional<Fonction> managerEntity = this.fonctionRepository
								.findById(element.getManagerIdFonction());
						Fonction childEntity = new Fonction();
						childEntity.setCreatedAt(LocalDateTime.now());
						childEntity.setCreatedBy(entityDto.getModifiedBy());
						childEntity.setLibelleFonction(element.getLibelleFonction());
						childEntity.setIsActive(element.getIsActive());
						childEntity.setDepartement(entity);
						childEntity.setDirection(parent);
						if (managerEntity.isPresent()) {
							childEntity.setManagerIdFonction(managerEntity.get());
						}
						entity.addFonction(childEntity);
						log.info("-- New entity Fonction added --");
					} else {
						Optional<Fonction> managerEntity = this.fonctionRepository
								.findById(element.getManagerIdFonction());
						Fonction childEntity = this.fonctionRepository.getById(element.getId());
						childEntity.setModifiedAt(LocalDateTime.now());
						childEntity.setModifiedBy(entityDto.getModifiedBy());
						childEntity.setLibelleFonction(element.getLibelleFonction());
						childEntity.setIsActive(element.getIsActive());
						childEntity.setDepartement(entity);
						childEntity.setDirection(parent);
						if (managerEntity.isPresent()) {
							childEntity.setManagerIdFonction(managerEntity.get());
						}
						log.info("-- Entity Fonction updated --");
					}
				});
			}
			entity.setModifiedBy(entityDto.getModifiedBy());
			entity.setModifiedAt(LocalDateTime.now());
			entity.setDirection(parent);
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
	public void deleteEntity(DepartementDto entityDto) throws SQLException {
		log.info("-- Delete entity Departement : Begin --");
		try {
			Departement entity = this.findById(entityDto.getId()).orElse(null);
			if (entity != null) {
				entity.getFonctions().stream().forEach(element -> {
					Fonction childEntity = this.fonctionRepository.findById(element.getId()).orElse(null);
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
				log.info("-- Delete entity Departement : End successfully --");
			}
		} catch (SQLException e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public List<Departement> findByDirection(Long directionId) throws SQLException {
		return null;
	}

	@Override
	public Optional<Departement> getByDirection(Long directionId, Long departementId) throws SQLException {
		log.info("-- Find entity Departement by Id : Begin --");
		try {
			log.info("-- Entity Departement Id : " + directionId + " found successfully --");
			return this.repository.getByDirection(directionId, departementId);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
		
	}
}
