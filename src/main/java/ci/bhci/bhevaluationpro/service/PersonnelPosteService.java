package ci.bhci.bhevaluationpro.service;

import java.util.List;

import ci.bhci.bhevaluationpro.domain.PersonnelPoste;

/**
 * Service interface for the PersonnelPoste entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-04
 */
public interface PersonnelPosteService extends AbstractBaseService<PersonnelPoste, Long> {

	List<PersonnelPoste> findByDirection(Long directionId);

	List<PersonnelPoste> findByDepartement(Long departementeId);

	List<PersonnelPoste> findByPersonnel(Long personnelId);

	boolean existPersonnelPoste(Long personnelId, Long fonctionId);

}
