package ci.bhci.bhevaluationpro.domain.dto;

import lombok.Data;

/**
 * Domain entity AbstractBaseEntity Data Transfer Object (Dto)
 *
 * @author kyao
 * @since 2022-02-03
 */

@Data
public class AbstractBaseEntityDto {
	private Boolean isActive;
	private Integer createdBy;
	private Integer modifiedBy;
	private Boolean isDeleted;
	private Integer deletedBy;
}
