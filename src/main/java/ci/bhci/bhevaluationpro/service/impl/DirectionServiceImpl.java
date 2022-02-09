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
import ci.bhci.bhevaluationpro.domain.dto.DepartementDto;
import ci.bhci.bhevaluationpro.domain.dto.DirectionDto;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.repository.DepartementRepository;
import ci.bhci.bhevaluationpro.repository.DirectionRepository;
import ci.bhci.bhevaluationpro.service.DirectionService;
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
@Log4j2
@Transactional
public class DirectionServiceImpl extends AbstractBaseRepositoryImpl<Direction, Long> implements DirectionService {

	private final DirectionRepository repository;
	private final DepartementRepository departementRepository;

	private final Transformer<DirectionDto, Direction> transformer = new Transformer<DirectionDto, Direction>(
			DirectionDto.class, Direction.class);

	private final Transformer<DepartementDto, Departement> departementTransformer = new Transformer<DepartementDto, Departement>(
			DepartementDto.class, Departement.class);

	@Autowired
	public DirectionServiceImpl(DirectionRepository repository, DepartementRepository departementRepository) {
		super(repository);
		this.repository = repository;
		this.departementRepository = departementRepository;
	}

	@Override
	@Transactional
	public DirectionDto addEntity(DirectionDto directionDto) throws SQLException {
		log.info("-- Add entity Direction : Begin --");
		try {
			List<Departement> children = new ArrayList<Departement>();
			Direction entity = this.transformer.convertToEntity(directionDto);
			if (directionDto.getDepartementDto().size() > 0) {
				directionDto.getDepartementDto().stream().forEach(departementDto -> {
					Departement childEntity = this.departementTransformer.convertToEntity(departementDto);
					childEntity.setDirection(entity);
					children.add(childEntity);
				});
//				entity.setModifiedAt(LocalDateTime.now());
				entity.setDepartements(children);
			}
			Direction newEntity = this.repository.save(entity);
			log.info("-- Add entity Direction : End successfully --");
			return this.transformer.convertToDto(newEntity);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public DirectionDto updateEntity(DirectionDto directionDto, Long id) throws SQLException {
		log.info("-- Update entity Direction : Begin --");
		try {
			Direction entity = this.findById(directionDto.getId()).orElse(null);
			if (directionDto.getDepartementDto().size() > 0) {
				directionDto.getDepartementDto().stream().forEach(departementDto -> {
					if (!this.departementRepository.existDepartement(id, departementDto.getLibelleDepartement())) {
						Departement childEntity = new Departement();
						childEntity.setCreatedAt(LocalDateTime.now());
						childEntity.setCreatedBy(directionDto.getModifiedBy());
						childEntity.setLibelleDepartement(departementDto.getLibelleDepartement());
						childEntity.setIsActive(departementDto.getIsActive());
						childEntity.setDirection(entity);
						entity.addDepartement(childEntity);
						log.info("-- New entity Departement added --");
					} else {
						if (this.departementRepository.findById(departementDto.getId()).isPresent()) {
							Departement childEntity = this.departementRepository.findById(departementDto.getId()).get();
							childEntity.setModifiedAt(LocalDateTime.now());
							childEntity.setModifiedBy(directionDto.getModifiedBy());
							childEntity.setLibelleDepartement(departementDto.getLibelleDepartement());
							childEntity.setIsActive(departementDto.getIsActive());
							childEntity.setDirection(entity);
							log.info("-- Entity Departement updated --");
						}
					}
				});
			}
			entity.setModifiedBy(directionDto.getModifiedBy());
			entity.setModifiedAt(LocalDateTime.now());
			entity.setCodeDirection(directionDto.getCodeDirection());
			entity.setLibelleDirection(directionDto.getLibelleDirection());
			entity.setIsActive(directionDto.getIsActive());
			Direction editedEntity = this.repository.save(entity);
			log.info("-- Update entity Direction : End successfully --");
			return this.transformer.convertToDto(editedEntity);
		} catch (SQLException e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void delete(DirectionDto directionDto, Long id) throws SQLException {
		log.info("-- Delete entity Direction : Begin --");
		try {
			Direction entity = this.findById(directionDto.getId()).orElse(null);
			if (entity != null) {
				entity.getDepartements().stream().forEach(departement -> {
					Departement childEntity = this.departementRepository.getById(departement.getId());
					childEntity.setIsDeleted(true);
					childEntity.setDeletedAt(LocalDateTime.now());
					childEntity.setDeletedBy(directionDto.getDeletedBy());
					childEntity.setIsActive(false);
					log.info("-- Entity Departement deleted --");
				});
				entity.setIsDeleted(true);
				entity.setDeletedAt(LocalDateTime.now());
				entity.setDeletedBy(directionDto.getDeletedBy());
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
	public Optional<Direction> getById(Long id) throws SQLException {
		log.info("-- Find entity Direction by Id : Begin --");
		try {
			log.info("-- Entity Direction Id : " + id + " found successfully --");
			return this.repository.findById(id);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

	@Override
	public List<DirectionDto> getAll() throws SQLException {
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
	public boolean existDirection(String codeDirection, String libelleDirection) throws SQLException {
		try {
			return this.repository.existDirection(codeDirection, libelleDirection);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public PaginationMod<DirectionDto> getAllDirections(Pageable pageable) {
//		Page<Direction> pagesOfentity = repository.findAll(pageable);
//		@SuppressWarnings("rawtypes")
//		PaginationMod paginationMod = new PaginationMod<Direction>(); // response of Pagination request
//
//		DirectionDto[] entityDtos = this.modelMapper.map(pagesOfentity.getContent(), DirectionDto[].class);
//		paginationMod.setValue(pagesOfentity, Arrays.asList(entityDtos));
//		return paginationMod;
//	}

}
