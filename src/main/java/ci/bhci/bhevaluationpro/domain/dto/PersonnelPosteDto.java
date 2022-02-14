/**
 * 
 */
package ci.bhci.bhevaluationpro.domain.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author kyao
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonnelPosteDto extends AbstractBaseEntityDto {
	private Long id;
	private Long idPersonnel;
	private Long IdFonction;
	private Date debutPoste;
	private Date finPoste;
}
