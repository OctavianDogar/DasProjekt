package edu.msg.jbook.backend.repository;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.Notification;
import edu.msg.jbook.backend.model.UserState;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by cioncag on 27.07.2016.
 */
@Local
public interface NotificationRepository extends BaseRepository<Notification, Long> {
    List<Notification> getNotificationsForUser(UserState user) throws RepositoryException;

    void removeNotificationsForEvent(Event event) throws RepositoryException;
}
