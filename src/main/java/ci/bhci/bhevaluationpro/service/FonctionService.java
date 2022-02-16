package ci.bhci.bhevaluationpro.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.domain.dto.FonctionDto;

/**
 * Service interface for the Fonction entity that extends the AbstractBaseService
 * 
 * @author kyao
 * @since 2022-01-04
 */
public interface FonctionService extends AbstractBaseService<Fonction, Long> {

	List<Fonction> findByDepartement(Long departementId) throws SQLException;

	List<Fonction> findByDirection(Long directionId) throws SQLException;

	boolean existFonction(Long directionId, Long departementId, Long managerIdFonction, String libelleFonction) throws SQLException;

	List<FonctionDto> getAll() throws SQLException;

	Optional<Fonction> getById(Long id) throws SQLException;

	FonctionDto addEntity(FonctionDto entityDto) throws SQLException;

	FonctionDto updateEntity(FonctionDto entityDto, Long id) throws SQLException;

	void delete(FonctionDto entityDto, Long id) throws SQLException;

	Optional<Fonction> getByDirection(Long idDirection, Long idDepartement, Long managerIdFonction) throws SQLException;

	Optional<Departement> getByDepartement(Long idDepartement, Long idFonction) throws SQLException;

	boolean existFonction1(Long idDirection, Long managerIdFonction, String libelleFonction) throws SQLException;

	boolean existFonction2(Long idDirection, Long idDepartement, String libelleFonction) throws SQLException;

	boolean existFonction3(Long idDirection, String libelleFonction) throws SQLException;

	Optional<Fonction> getByDirectionAndDepartement(Long idDirection, Long idDepartement);

	boolean isManager(Long idFonction, Long managerIdFonction);

}
