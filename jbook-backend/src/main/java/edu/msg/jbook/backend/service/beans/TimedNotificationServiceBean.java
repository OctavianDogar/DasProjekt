package edu.msg.jbook.backend.service.beans;

import edu.msg.jbook.backend.assembler.ProfileAssembler;
import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.EventRepository;
import edu.msg.jbook.backend.repository.UserStateRepository;
import edu.msg.jbook.backend.service.EventService;
import edu.msg.jbook.backend.service.NotificationService;
import edu.msg.jbook.backend.service.TimedNotificationService;
import edu.msg.jbook.backend.service.UserService;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by ilyesk on 09.08.2016.
 */
@Singleton
public class TimedNotificationServiceBean implements TimedNotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(EventServiceBean.class);
    @EJB
    UserStateRepository usr;
    @EJB
    EventRepository ev;
    @EJB
    UserService us;
    @EJB
    NotificationService ns;
    @EJB
    ProfileAssembler pa;
    @EJB
    EventRepository er;
    @EJB
    EventService es;

    @Override
    @Schedule(hour = "0", minute = "0", second = "1")
    public void addBirthDayNotification() throws ServiceException {
        List<UserState> users = null;
        Date date = null;
        List<UserState> userStates = null;
        try {
            date = new Date();
            userStates = usr.getAll();
            users = new LinkedList<>();
            for (UserState s : userStates) {
                if (getDay(s.getUser().getBirthday()).equals(getDay(date)) && getMonth(s.getUser().getBirthday()).equals(getMonth(date))) {
                    users.add(s);
                }
            }
            for (UserState u : users) {
                ns.addBirthdayNotifications(pa.modelToDto(u), pa.modelToDto(u));
                notifyFriends(u);
            }
        } catch (RepositoryException | ServiceException | CommonException e) {
            LOG.error("Timed birthday notification failed.");
            throw new ServiceException("Timed birthday notification failed.", e);
        }
    }

    @Override
    @Schedule(minute = "*/10",hour = "*",second = "*")
    public void addDayBeforeEventNotification() throws ServiceException {
        List<Event> eventList = null;
        List<Event> eventListInADay = null;
        LocalDateTime date = null;
        List<ProfileDto> usersGoing = null;
        try {
            date = new LocalDateTime();
            eventListInADay = new LinkedList<>();
            eventList = er.getAll();
            for (Event s : eventList) {
                LocalDateTime temp = new LocalDateTime().plusDays(2);
                Date tempDate = temp.toDate();
                if (date.toDate().before(Date.from(s.getDateTime().toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())) && temp.toDate().after(Date.from(s.getDateTime().toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))) {
                    if (s.getDateTime().getHour() == date.getHourOfDay()) {
                        eventListInADay.add(s);
                    }
                }
            }
            for (Event e : eventListInADay) {
                usersGoing = es.getParticipantsByEventId(e.getId());
                for (ProfileDto p : usersGoing) {
                    notifyParticipants(p, e);
                }
            }
        } catch (RepositoryException | ServiceException e) {
            LOG.error("Timed event notification failed.");
            throw new ServiceException("Timed event notification failed.", e);
        }
    }

    private void notifyFriends(UserState userState) {
        List<ProfileDto> friendsDto = null;
        try {
            friendsDto = us.getUserFriendsByEmail(userState.getEmail());
            for (ProfileDto f : friendsDto) {
                ns.addBirthdayNotifications(pa.modelToDto(userState), f);
            }
        } catch (ServiceException | CommonException e) {
            LOG.error("Failed to get friends.");
            throw new ServiceException("Failed to get friends.", e);
        }
    }

    private void notifyParticipants(ProfileDto p, Event e) {
        try {
            ns.setNotificationsForEventParticipants(p, e.getId());
        } catch (ServiceException se) {
            LOG.error("Failed to notify participants.");
            throw new ServiceException("Failed to notify participants.", se);
        }
    }

    private String getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);

        return Integer.toString(month);
    }

    private String getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return Integer.toString(day);
    }
}
