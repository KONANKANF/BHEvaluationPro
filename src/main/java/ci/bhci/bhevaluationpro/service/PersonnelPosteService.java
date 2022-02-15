package ci.bhci.bhevaluationpro.service;

import java.sql.SQLException;
import java.util.List;

import ci.bhci.bhevaluationpro.domain.PersonnelPoste;

/**
 * Service interface for the PersonnelPoste entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-04
 */
public interface PersonnelPosteService extends AbstractBaseService<PersonnelPoste, Long> {

	List<PersonnelPoste> findByDirection(Long directionId) throws SQLException;

	List<PersonnelPoste> findByDepartement(Long departementeId) throws SQLException;

	List<PersonnelPoste> findByPersonnel(Long personnelId) throws SQLException;

	boolean existPersonnelPoste(Long personnelId, Long fonctionId) throws SQLException;

}
