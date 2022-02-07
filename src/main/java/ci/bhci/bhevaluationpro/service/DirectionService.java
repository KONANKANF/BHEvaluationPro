package ci.bhci.bhevaluationpro.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.Direction;
import ci.bhci.bhevaluationpro.domain.dto.DirectionDto;

/**
 * Service interface for the Direction entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-02-03
 */
public interface DirectionService extends AbstractBaseService<Direction, Long> {
	
	boolean existDirection(String codeDirection, String libelleDirection);

	Optional<DirectionDto> getById(Long id) throws SQLException;

	List<DirectionDto> getAll() throws SQLException;

	DirectionDto addEntity(DirectionDto entityDto) throws SQLException; 
}
