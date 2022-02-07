package ci.bhci.bhevaluationpro.domain.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain entity Personnel Dto extends class AbstractBaseEntityDto
 *
 * @author kyao
 * @since 2022-02-03
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonnelDto extends AbstractBaseEntityDto {

	private Long id;
	private String matricule;
	private String civilite;
	private String nomPrenoms;
	private String email;
	private String photo;
	private Date dateNaissance;
	private Date dateEmbauche;
	private List<PersonnelPosteDto> personnelPosteDto = new ArrayList<>();
	private Boolean isActivate;
}
