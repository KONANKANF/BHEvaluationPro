package ci.bhci.bhevaluationpro.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.PersonnelPoste;
import ci.bhci.bhevaluationpro.domain.dto.PersonnelPosteDto;

/**
 * Service interface for the PersonnelPoste entity that extends the
 * AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-04
 */
public interface PersonnelPosteService extends AbstractBaseService<PersonnelPoste, Long> {

	List<PersonnelPosteDto> findByDirection(Long directionId) throws SQLException;

	List<PersonnelPosteDto> findByDepartement(Long departementeId) throws SQLException;

	List<PersonnelPosteDto> findByPersonnel(Long personnelId) throws SQLException;

	Optional<PersonnelPoste> findById(Long id) throws SQLException;

	PersonnelPosteDto addEntity(PersonnelPosteDto entityDto) throws SQLException;

	PersonnelPosteDto updateEntity(PersonnelPosteDto entityDto, Long id) throws SQLException;

	void deleteEntity(PersonnelPosteDto personnelPoste) throws SQLException;

	boolean existPersonnelPoste(Long personnelId, Long fonctionId) throws SQLException;

	List<PersonnelPosteDto> findByPesonnelAndFonction(Long idPersonnel, Long idFonction) throws SQLException;

	List<PersonnelPosteDto> getAll() throws SQLException;

}
