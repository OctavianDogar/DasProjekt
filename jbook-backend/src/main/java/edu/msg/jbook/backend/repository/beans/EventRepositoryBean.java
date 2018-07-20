package edu.msg.jbook.backend.repository.beans;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by dogaro on 27/07/2016.
 */
@Stateless(name = "EventRepository", mappedName = "ejb/EventRepository")
public class EventRepositoryBean extends BaseRepositoryBean<Event, Long> implements EventRepository {

    private static final Logger LOG = LoggerFactory.getLogger(EventRepositoryBean.class);
    @PersistenceContext(unitName = "jbook")
    private EntityManager em;

    public EventRepositoryBean() {
        super(Event.class);
    }

    @Override
    public List<Event> getEventsByAdmin(String email) {
        try {
            Query query = em.createQuery("SELECT e from Event e where e.userAdmin.email=?1 and e.userAdmin.accountStatus =?2");
            query.setParameter(1, email);
            query.setParameter(2, false);
            return query.getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get by dynamic admin.", e);
            throw new RepositoryException("Cannot get by dynamic admin.", e);
        }
    }

    @Override
    public Event getEventByName(String name) throws RepositoryException {
        try{
            Query query = em.createQuery("select e from Event e where e.title=?1 and e.userAdmin.accountStatus =?2");
            query.setParameter(1,name);
            query.setParameter(2, false);
            return (Event) query.getSingleResult();
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get event by name.", e);
            throw new RepositoryException("Cannot get event by name.", e);
        }
    }

    @Override
    public List<Event> getAll() throws RepositoryException {
        try{
            Query query = em.createQuery("select e from Event e where e.userAdmin.accountStatus =?1");
            query.setParameter(1, false);
            return query.getResultList();
        }catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get all events.", e);
            throw new RepositoryException("Cannot get all events.", e);
        }
    }

    @Override
    public Event getById(Long id) throws RepositoryException {
        try{
            Query query = em.createQuery("select e from Event e where e.id =?1 and e.userAdmin.accountStatus =?2");
            query.setParameter(1, id);
            query.setParameter(2, false);
            return (Event) query.getSingleResult();
        }catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get all events.", e);
            throw new RepositoryException("Cannot get all events.", e);
        }
    }

    @Override
    public UserState getAdminByEvent(Long eventId) throws RepositoryException {
        try{
            Query query = em.createQuery("select e.userAdmin from Event e where e.id=?1 and e.userAdmin.accountStatus =?2");
            query.setParameter(1, eventId);
            query.setParameter(2, false);
            return (UserState) query.getSingleResult();
        }catch(IllegalArgumentException | PersistenceException e){
            LOG.error("Cannot get event admin.", e);
            throw new RepositoryException("Cannot get event admin.", e);
        }
    }
}
