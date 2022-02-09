package ci.bhci.bhevaluationpro.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain entity Departement Dto extends class AbstractBaseEntityDto
 *
 * @author kyao
 * @since 2022-02-03
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartementDto extends AbstractBaseEntityDto {
	private Long id;	
	private Long directionId;
	private String libelleDepartement;
	private List<FonctionDto> fonctionDto = new ArrayList<>();
}
