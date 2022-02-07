package ci.bhci.bhevaluationpro.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain entity Direction Dto extends class AbstractBaseEntityDto
 *
 * @author kyao
 * @since 2022-02-03
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectionDto extends AbstractBaseEntityDto {
	private Long id;
	private String codeDirection;
	private String libelleDirection;
	private List<DepartementDto> departementDto = new ArrayList<>();
	private List<FonctionDto> fonctionDto = new ArrayList<>();
}
