package ci.bhci.bhevaluationpro.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain entity Fonction Dto extends class AbstractBaseEntityDto
 *
 * @author kyao
 * @since 2022-02-03
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FonctionDto extends AbstractBaseEntityDto {
	private Long id;
	private String libelleFonction;
	private Long idDirection;
	private Long idDepartement;
	private Long managerIdFonction;
	private List<PersonnelPosteDto> personnelPosteDto = new ArrayList<>();
}
