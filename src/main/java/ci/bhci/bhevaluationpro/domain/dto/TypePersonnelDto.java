package ci.bhci.bhevaluationpro.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain entity TypePersonnel Dto extends class AbstractBaseEntityDto
 *
 * @author kyao
 * @since 2022-02-03
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypePersonnelDto extends AbstractBaseEntityDto {
	private Long id;
	private String libelleType;
}
