package edu.msg.jbook.backend.repository.beans;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.UserEvent;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.UserEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by iacobd on 27.07.2016.
 */
@Stateless(name = "UserEventRepository", mappedName = "ejb/UserEventRepository")
public class UserEventRepositoryBean extends BaseRepositoryBean<UserEvent, Long> implements UserEventRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserEventRepositoryBean.class);
    @PersistenceContext(unitName = "jbook")
    private EntityManager entityManager;

    public UserEventRepositoryBean() {
        super(UserEvent.class);
    }

    @Override
    public List<UserEvent> getUserEventsByEvent(Event event) throws RepositoryException {
        try {
            Query query = entityManager.createQuery("SELECT u from UserEvent u where u.event.id = ?1 " +
                    "and u.event.userAdmin.accountStatus=?2 and u.userState.accountStatus=?3");
            query.setParameter(1, event.getId());
            query.setParameter(2, false);
            query.setParameter(3, false);
            return query.getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user events by event.", e);
            throw new RepositoryException("Cannot get user events by event.", e);
        }
    }

    @Override
    public List<UserEvent> getUserEventsByUser(UserState userState) throws RepositoryException {
        try {
            Query query = entityManager.createQuery("select u from UserEvent u where u.userState.id = ?1 " +
                    "and u.event.userAdmin.accountStatus=?2 and u.userState.accountStatus=?3");
            query.setParameter(1, userState.getId());
            query.setParameter(2, false);
            query.setParameter(3, false);
            return query.getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user events by user.", e);
            throw new RepositoryException("Cannot get user events by user.", e);
        }
    }

    @Override
    public List<UserEvent> getUserEventsByUserEmail(String email) throws RepositoryException {
        try {
            Query query = entityManager.createQuery("select u from UserEvent u where u.userState.email = ?1 " +
                    "and u.event.userAdmin.accountStatus=?2 and u.userState.accountStatus=?3");
            query.setParameter(1, email);
            query.setParameter(2, false);
            query.setParameter(3, false);
            return query.getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user events by email.", e);
            throw new RepositoryException("Cannot get user events by email.", e);
        }
    }

    @Override
    public List<UserEvent> getAll() throws RepositoryException {
        try {
            Query query = entityManager.createQuery("select u from UserEvent u where " +
                    "u.event.userAdmin.accountStatus=?1 and u.userState.accountStatus=?2");
            query.setParameter(1, false);
            query.setParameter(2, false);
            return query.getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user events by email.", e);
            throw new RepositoryException("Cannot get user events by email.", e);
        }
    }

    @Override
    public UserEvent getById(Long id) throws RepositoryException {
        try {
            Query query = entityManager.createQuery("select u from UserEvent u where u.id=?1 and " +
                    "u.event.userAdmin.accountStatus=?2 and u.userState.accountStatus=?3");
            query.setParameter(1, id);
            query.setParameter(2, false);
            query.setParameter(3, false);
            return (UserEvent) query.getSingleResult();
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user events by email.", e);
            throw new RepositoryException("Cannot get user events by email.", e);
        }
    }
}
