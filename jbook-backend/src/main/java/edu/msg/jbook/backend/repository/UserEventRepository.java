package edu.msg.jbook.backend.repository;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.UserEvent;
import edu.msg.jbook.backend.model.UserState;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by iacobd on 27.07.2016.
 */
@Local
public interface UserEventRepository extends BaseRepository<UserEvent, Long> {
    List<UserEvent> getUserEventsByEvent(Event event) throws RepositoryException;

    List<UserEvent> getUserEventsByUser(UserState userState) throws RepositoryException;

    List<UserEvent> getUserEventsByUserEmail(String email) throws RepositoryException;
}
