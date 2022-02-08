package ci.bhci.bhevaluationpro.service.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.bhci.bhevaluationpro.domain.AbstractBaseEntity;
import ci.bhci.bhevaluationpro.repository.AbstractBaseRepository;
import ci.bhci.bhevaluationpro.service.AbstractBaseService;

/**
 * Implementation of Service interface for the AbstractEntity entity that extends the
 * 
 * @author kyao
 *
 * @param <T>
 * @param <ID>
 */
@Service
@Transactional
public abstract class AbstractBaseRepositoryImpl<T extends AbstractBaseEntity, ID extends Serializable>
        implements AbstractBaseService<T, ID>{
    
    private final AbstractBaseRepository<T, ID> abstractBaseRepository;
    
    @Autowired
    public AbstractBaseRepositoryImpl(AbstractBaseRepository<T, ID> abstractBaseRepository) {
        this.abstractBaseRepository = abstractBaseRepository;
    }
    
    @Override
    public T save(T entity) throws SQLException {
        return (T) abstractBaseRepository.save(entity);
    }

    @Override
    public List<T> findAll() throws SQLException {
        return abstractBaseRepository.findAll();
    }

    @Override
    public Optional<T> findById(ID entityId) throws SQLException  {
    		return abstractBaseRepository.findById(entityId);     
    }

    @Override
    public T update(T entity) throws SQLException {
        return (T) abstractBaseRepository.save(entity);
    }

    @Override
    public T updateById(T entity, ID entityId) {
        Optional<T> optional = abstractBaseRepository.findById(entityId);
        if(optional.isPresent()){
            return (T) abstractBaseRepository.save(entity);
        }else{
            return null;
        }
    }

    @Override
    public void delete(T entity) throws SQLException {
        abstractBaseRepository.delete(entity);
    }

    @Override
    public void deleteById(ID entityId) throws SQLException {
        abstractBaseRepository.deleteById(entityId);
    }

}