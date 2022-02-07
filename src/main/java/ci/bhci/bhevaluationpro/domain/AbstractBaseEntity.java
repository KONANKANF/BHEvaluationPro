package ci.bhci.bhevaluationpro.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * BaseEntity class for entities
 * 
 * @author kyao
 * @since 2022-01-04
 */

@Data
@AllArgsConstructor
//@NoArgsConstructor
@MappedSuperclass
public class AbstractBaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Version
	private int version;
	@Column(name = "isactive")
	private Boolean isActive;
	@Column(nullable = true, updatable = false)
	private Integer createdBy;
	@Column(length = 32, nullable = true, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;
	private Integer modifiedBy;
	@LastModifiedDate
	private LocalDateTime modifiedAt;
	private Boolean isDeleted;
	private Integer deletedBy;
	private LocalDateTime deletedAt;

    public AbstractBaseEntity() {
        this.createdAt = LocalDateTime.now();
//        this.modifiedAt = LocalDateTime.now();
    }    
}