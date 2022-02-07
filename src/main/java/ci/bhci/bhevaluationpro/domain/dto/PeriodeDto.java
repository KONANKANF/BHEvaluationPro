package ci.bhci.bhevaluationpro.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain entity Periode Dto extends class AbstractBaseEntityDto
 *
 * @author kyao
 * @since 2022-02-03
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeriodeDto extends AbstractBaseEntityDto {
	private Long id;
	private String codePeriode;
	private String yearPeriode;
	private String debutPeriode;
	private String finPeriode;
	private String libellePeriode;
	private Long statutId;
}
