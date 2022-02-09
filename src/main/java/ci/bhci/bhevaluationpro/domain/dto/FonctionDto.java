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
	private Long directionId;
	private Long departementId;
	private Long managerIdFonction;
//	private List<FonctionDto> collabotateurDto = new ArrayList<>(); 
	private List<PersonnelPosteDto> personnelPosteDto = new ArrayList<>();
//	private Date debutPoste;
//	private Date finPoste;
}
