package ci.bhci.bhevaluationpro.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.bhci.bhevaluationpro.domain.Periode;

/**
 * Repository for the Periode entity that extends the AbstractBaseRepository
 * 
 * @author kyao
 * @since 2022-02-03
 */

@Repository
public interface PeriodeRepository extends AbstractBaseRepository<Periode, Long> {

	@Query("select case when count(p)> 0 then true else false end from Periode p where lower(libelle_periode) like lower(:libelle) and year_periode = :annee and debut_periode = :periodeDebut and fin_periode = :periodeFin and isactive= 1 and is_deleted is null")
	boolean existPeriode(@Param("libelle") String libelleTypeEvaluation, @Param("annee") String annee,
			@Param("periodeDebut") Date periodeDebut, @Param("periodeFin") Date periodeFin);

	@Query("select p from Periode p where id_periode = :periodeId and isactive = 1 and is_deleted is null")
	Optional<Periode> findById(@Param("periodeId") Long periodeId);

	@Query("select p from Periode p where lower(libelle_periode) like lower(:libelle) and isactive= 1 and is_deleted is null")
	Optional<Periode> findByLibellePeriode(@Param("libelle") String libelleTypeEvaluation);

	@Query("select p from Periode p where isactive = 1 and is_deleted is null")
	List<Periode> findAll();
}