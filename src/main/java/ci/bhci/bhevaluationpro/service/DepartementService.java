package ci.bhci.bhevaluationpro.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.dto.DepartementDto;

/**
 * Service interface for the Departement entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-08
 */
public interface DepartementService extends AbstractBaseService<Departement, Long> {

	List<Departement> findByDirection(Long directionId) throws SQLException;
	
	boolean existDepartement(Long directionId, String libelleDepartement) throws SQLException;

	List<DepartementDto> getAll() throws SQLException;

	Optional<Departement> getById(Long id) throws SQLException;

	DepartementDto addEntity(DepartementDto entityDto) throws SQLException;

	DepartementDto updateEntity(DepartementDto entityDto, Long id) throws SQLException;

	void delete(DepartementDto entityDto, Long id) throws SQLException;

	Optional<Departement> getByDirection(Long idDirection, Long departementId) throws SQLException; 
}
