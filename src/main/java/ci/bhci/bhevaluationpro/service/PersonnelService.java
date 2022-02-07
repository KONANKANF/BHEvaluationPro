package ci.bhci.bhevaluationpro.service;

import java.util.List;

import ci.bhci.bhevaluationpro.domain.Personnel;

/**
 * Service interface for the Personnel entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-04
 */
public interface PersonnelService extends AbstractBaseService<Personnel, Long> {

	List<Personnel> findByDepartement(Long departementeId);

	List<Personnel> findByDirection(Long directionId);

	List<Personnel> findByFonction(Long fonctionId);

	List<Personnel> findByManager(Long managerIdFonction);

	List<Personnel> findByPersonnelPoste(Long personnelPosteId);

	boolean existPersonnel(String matricule, String email);

}
