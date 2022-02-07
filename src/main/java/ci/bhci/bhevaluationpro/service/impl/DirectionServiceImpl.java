package ci.bhci.bhevaluationpro.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.Direction;
import ci.bhci.bhevaluationpro.domain.dto.DirectionDto;
import ci.bhci.bhevaluationpro.exception.CustomDataNotFoundException;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.repository.DirectionRepository;
import ci.bhci.bhevaluationpro.service.DirectionService;
import ci.bhci.bhevaluationpro.transformer.Transformer;
import ci.bhci.bhevaluationpro.util.PaginationMod;
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
//	private final ModelMapper modelMapper;
	private final Transformer<DirectionDto, Direction> transformer = new Transformer<DirectionDto, Direction>(
			DirectionDto.class, Direction.class);

	@Autowired
	public DirectionServiceImpl(DirectionRepository repository) {
		super(repository);
		this.repository = repository;
//		this.modelMapper = modelMapper;
	}

	@Transactional
	public Direction save(Direction direction) {
		log.info("-- Begin add new Direction --");
		try {
			List<Departement> departements = new ArrayList<Departement>();
			if (direction.getDepartements().size() > 0) {
				direction.getDepartements().stream().forEach(departement -> {
					departement.setDirection(direction);
					departements.add(departement);
				});
				direction.setDepartements(departements);
			}
			Direction newDirection = this.repository.save(direction);
			log.info("-- End add new Categoey --");
			return newDirection;
		} catch (Exception e) {
	        throw new CustomErrorException(e.getMessage());
	    }
		
	}

	@Override
	public Optional<DirectionDto> getById(Long id) throws SQLException {
		try {
			return Optional.of(this.transformer.convertToDto(this.repository.findById(id)));
			
		} catch (Exception e) {
			throw new CustomErrorException(e.getMessage());
		}
	}
	
	@Override
	public List<DirectionDto> getAll() throws SQLException {
		try {
			return this.transformer.convertToDto(this.repository.findAll());
			
		} catch (Exception e) {
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
