package ci.bhci.bhevaluationpro.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ci.bhci.bhevaluationpro.domain.AbstractBaseEntity;

/**
 * Generic JPA Repository interface
 * 
 * @author kyao
 *
 * @param <T>
 * @param <ID>
 */

@NoRepositoryBean
public interface AbstractBaseRepository<T extends AbstractBaseEntity, ID extends Serializable>
		extends JpaRepository<T, ID> {

}