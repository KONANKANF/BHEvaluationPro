package ci.bhci.bhevaluationpro.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.Direction;
import ci.bhci.bhevaluationpro.domain.dto.DepartementDto;
import ci.bhci.bhevaluationpro.domain.dto.DirectionDto;
import ci.bhci.bhevaluationpro.exception.CustomAlreadyExistsException;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
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
	private final Transformer<DirectionDto, Direction> transformer = new Transformer<DirectionDto, Direction>(
			DirectionDto.class, Direction.class);

	private final Transformer<DepartementDto, Departement> departementTransformer = new Transformer<DepartementDto, Departement>(
			DepartementDto.class, Departement.class);

	@Autowired
	public DirectionServiceImpl(DirectionRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	@Transactional
	public DirectionDto addEntity(DirectionDto directionDto) throws SQLException {
		log.info("-- Add new entity Direction : Begin --");
		try {
			List<Departement> departements = new ArrayList<Departement>();
			Direction direction = this.transformer.convertToEntity(directionDto);
			if (directionDto.getDepartementDto().size() > 0) {
				directionDto.getDepartementDto().stream().forEach(departementDto -> {
					Departement departement = this.departementTransformer.convertToEntity(departementDto);
					departement.setDirection(direction);
					departements.add(departement);
				});
				direction.setDepartements(departements);
			}
			Direction newDirection = this.repository.save(direction);
			log.info("-- Add new entity Direction : End successfully --");
			return this.transformer.convertToDto(newDirection);
		} catch (Exception e) {
			log.error("SQLErreur -> " + e.getMessage());
			throw new CustomErrorException(e.getMessage());
		}

	}

	@Override
	public Optional<DirectionDto> getById(Long id) throws SQLException {
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
	public boolean existDirection(String codeDirection, String libelleDirection) {
		return this.repository.existDirection(codeDirection, libelleDirection);
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
