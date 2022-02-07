package ci.bhci.bhevaluationpro.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain entity Statut Dto extends class AbstractBaseEntityDto
 *
 * @author kyao
 * @since 2022-02-03
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatutDto extends AbstractBaseEntityDto {
	private Long id;
	private String libelleStatut;
	private String descriptionStatut;
}
