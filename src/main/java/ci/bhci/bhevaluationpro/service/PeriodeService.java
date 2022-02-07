
package ci.bhci.bhevaluationpro.service;

import java.util.Date;
import java.util.Optional;

import ci.bhci.bhevaluationpro.domain.Periode;

/**
 * PeriodeService
 *
 * @author kyao
 * @since 2022-01-20
 */
public interface PeriodeService extends AbstractBaseService<Periode, Long> {
	
	/**
	 * @param libellePeriode
	 * @param annee
	 * @param periodeDebut
	 * @param periodeFin
	 * @return
	 */
	boolean existPeriode(String libellePeriode, String annee, Date periodeDebut, Date periodeFin);

	/**
	 * @param libellePeriode
	 * @return
	 */
	Optional<Periode> findByLibellePeriode(String libellePeriode, String annee, Date periodeDebut, Date periodeFin);



}
