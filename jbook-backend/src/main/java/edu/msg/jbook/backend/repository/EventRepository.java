package edu.msg.jbook.backend.repository;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.UserState;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by dogaro on 27/07/2016.
 */
@Local
public interface EventRepository extends BaseRepository<Event, Long> {

    List<Event> getEventsByAdmin(String email) throws RepositoryException;

    Event getEventByName(String name) throws RepositoryException;

    UserState getAdminByEvent(Long eventId) throws RepositoryException;

}
