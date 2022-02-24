package ci.bhci.bhevaluationpro.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.Personnel;
import ci.bhci.bhevaluationpro.domain.dto.PersonnelDto;

/**
 * Service interface for the Personnel entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-04
 */
public interface PersonnelService extends AbstractBaseService<Personnel, Long> {

	List<PersonnelDto> findByDepartement(Long departementeId) throws SQLException;

	List<PersonnelDto> findByDirection(Long directionId) throws SQLException;

	List<PersonnelDto> findByFonction(Long fonctionId) throws SQLException;

	List<PersonnelDto> findByManager(Long managerIdFonction) throws SQLException;

	PersonnelDto findByPersonnelPoste(Long personnelPosteId) throws SQLException;
	
	
	Optional<Personnel> getById(Long id) throws SQLException;

	PersonnelDto addEntity(PersonnelDto entityDto) throws SQLException;

	PersonnelDto updateEntity(PersonnelDto entityDto) throws SQLException;

	void deleteEntity(PersonnelDto entityDto) throws SQLException;

	boolean existPersonnel(String matricule, String email) throws SQLException;

	List<PersonnelDto> getAll() throws SQLException;

}
