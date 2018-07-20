package edu.msg.jbook.backend.repository.beans;

import edu.msg.jbook.backend.model.AbstractEntity;
import edu.msg.jbook.backend.repository.BaseRepository;
import edu.msg.jbook.backend.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by cioncag on 26.07.2016.
 */
public class BaseRepositoryBean<T extends AbstractEntity, I> implements BaseRepository<T, I> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseRepositoryBean.class);
    @PersistenceContext(unitName = "jbook")
    private EntityManager entityManager;
    private Class<T> classType;

    public BaseRepositoryBean(Class<T> classType) {
        this.classType = classType;
    }

    public List<T> getAll()throws RepositoryException {
        try{
            final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            final CriteriaQuery<T> cq = cb.createQuery(classType);
            final Root<T> rootEntry = cq.from(classType);
            final CriteriaQuery<T> all = cq.select(rootEntry);
            final TypedQuery<T> allQuery = entityManager.createQuery(all);
            return allQuery.getResultList();
        }catch(IllegalArgumentException | PersistenceException e){
            LOG.error("Get all failed.",e);
            throw new RepositoryException("Get all failed.",e);
        }
    }

    public T save(T item)throws RepositoryException{
        System.out.println("Service: ");
        try{
            entityManager.persist(item);
            entityManager.flush();
            entityManager.refresh(item);
            return item;
        }catch(IllegalArgumentException | PersistenceException ex){
            LOG.error("Save operation failed",ex);
            throw new RepositoryException("Save operation failed",ex);
        }
    }

    public T merge(T item)throws RepositoryException{
        try{
            item = entityManager.merge(item);
            entityManager.flush();
            return item;
        }catch(IllegalArgumentException | PersistenceException ex){
            LOG.error("Merge operation failed",ex);
            throw new RepositoryException("Merge operation failed",ex);
        }
    }

    public void delete(T item)throws RepositoryException{
        try{
            entityManager.remove(item);
            entityManager.flush();
        }catch(IllegalArgumentException | PersistenceException ex){
            LOG.error("Delete operation failed",ex);
            throw new RepositoryException("Delete operation failed",ex);
        }
    }

    public T getById(I id)throws RepositoryException{
        try {
            return entityManager.find(classType, id);
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Get by id failed.",e);
            throw new RepositoryException("Get by id failed.", e);
        }
    }

}
