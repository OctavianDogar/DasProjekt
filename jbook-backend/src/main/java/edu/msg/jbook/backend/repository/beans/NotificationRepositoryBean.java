package edu.msg.jbook.backend.repository.beans;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.Notification;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by cioncag on 27.07.2016.
 */

@Stateless(name = "NotificationRepository", mappedName = "ejb/NotificationRepository")
public class NotificationRepositoryBean extends BaseRepositoryBean<Notification, Long> implements NotificationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationRepositoryBean.class);
    @PersistenceContext(unitName = "jbook")
    private EntityManager entityManager;

    public NotificationRepositoryBean() {

        super(Notification.class);
    }

    @Override
    public List<Notification> getNotificationsForUser(UserState user) throws RepositoryException {
        try {
            Query query = entityManager.createQuery("Select e from Notification e where e.target.id =?1 and " +
                    "e.author.accountStatus=?2 order by e.time DESC");
            query.setParameter(1,user.getId());
            query.setParameter(2,false);
            return query.getResultList();
        } catch (IllegalArgumentException | PersistenceException e){
            LOG.error("Cannot get by User.",e);
            throw new RepositoryException("Cannot get by User.",e);
        }
    }

    @Override
    public void removeNotificationsForEvent(Event event) throws RepositoryException {
        try {
            Query query = entityManager.createQuery("Select e from Notification e where e.event.id =?1").setParameter(1, event.getId());
            List<Notification> notifications = query.getResultList();

            for(Notification notification : notifications) {
                super.delete(notification);
            }
        } catch (IllegalArgumentException | PersistenceException e){
            LOG.error("Cannot delete notifications for event.",e);
            throw new RepositoryException("Cannot delete notifications for event.", e);
        }
    }

}