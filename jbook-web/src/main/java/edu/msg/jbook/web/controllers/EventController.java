package edu.msg.jbook.web.controllers;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.service.EventService;
import edu.msg.jbook.backend.service.UserService;
import edu.msg.jbook.common.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dogaro on 29/07/2016.
 */
@ManagedBean
@Stateless
@DependsOn("EventService")
public class EventController implements Serializable {

    private final Logger LOG = LoggerFactory.getLogger(EventController.class);
    @EJB
    private UserController userController;
    @EJB
    private EventService eventService;
    @EJB
    private UserService userService;

    public List<EventDto> getEventsByAdmin(String email) {
        List<EventDto> events = null;
        try {
            events = eventService.getEventsByAdmin(email);
        } catch (ServiceException e) {
            LOG.error("Cannot get events by admin", e);
        }
        return events;
    }

    public List<EventDto> getEventsByParticipant(ProfileDto profileDto) {
        List<EventDto> eventDtos = null;
        try {
            eventDtos = eventService.getEventsByParticipant(profileDto);
        } catch (ServiceException e) {
            LOG.error("Cannot get events by participant.", e);
        }
        return eventDtos;
    }

    public EventDto getEventById(Long id) {
        EventDto eventDto = null;
        try {
            eventDto = eventService.getEventById(id);
        } catch (ServiceException e) {
            LOG.error("Cannot get event by id.", e);
        }
        return eventDto;
    }

    public List<ParticipantStatusDto> getParticipantsAndSatusesByEventId(Long id) {
        List<ParticipantStatusDto> participantStatusDtos = null;
        try {
            participantStatusDtos = eventService.getParticipantsAndSatusesByEventId(id);
        } catch (ServiceException e) {
            LOG.error("Cannot get participants and their statuses by event id.", e);
        }
        return participantStatusDtos;
    }

    public List<ParticipantStatusDto> getGoingParticipantsAndSatuses(Long id) {
        List<ParticipantStatusDto> participantStatusDtos = null;
        try {
            participantStatusDtos = getParticipantsAndSatusesByEventId(id).stream().filter(psd -> psd.isStatus()).collect(Collectors.toList());
        } catch (ServiceException e) {
            LOG.error("Cannot get going participants and statuses.", e);
        }
        return participantStatusDtos;
    }

    public List<ParticipantStatusDto> getNotGoingParticipantsAndSatuses(Long id) {
        return getParticipantsAndSatusesByEventId(id).stream().filter(psd -> !psd.isStatus()).collect(Collectors.toList());
    }

    public void createEvent(CreateEventDto eventDto) {
        try {
            eventDto.setAdminEmail(userController.getCurrentUser().getEmail());
            eventService.insertEvent(eventDto);
        }catch (ServiceException e){
            LOG.error("Cannot create event.",e);
        }
    }

    public List<PostDto> getEventPosts(Long id) {
        List<PostDto> posts = null;
        try {
            posts = eventService.getEventPostsByEventId(id);
        } catch (ServiceException e) {
            LOG.error("Get post to event failed", e);
        }
        return posts.stream()
                .sorted((o1, o2) -> o2.getCreationTime()
                        .compareTo(o1.getCreationTime()))
                .collect(Collectors.toList());
    }

    public List<EventDto> getAllEvents() {
        List<EventDto> eventDtos = null;
        try {
            eventDtos = eventService.getAllEvents();
        } catch (ServiceException e) {
            LOG.error("Cannot get all events.", e);
        }
        return eventDtos;
    }

    public Integer getParticipantsNumber(Long id) {
        Integer num = null;
        try{
            num = eventService.participantsNumber(id);
        }catch(ServiceException e){
            LOG.error("Cannot get participants number.",e);
        }
        return num;
    }

    public Map<Long, Integer> getAllParticipantsNumbers() {
        List<EventDto> allEvents = null;
        Map<Long, Integer> mapped = null;
        try {
            allEvents = eventService.getAllEvents();
            mapped = new HashMap<>();
            for (EventDto edt : allEvents) {
                mapped.put(edt.getId(), getParticipantsNumber(edt.getId()));
            }
        } catch (ServiceException e) {
            LOG.error("Cannot get participants number.", e);
        }
        return mapped;
    }

    public void removeEvent(Long id) {
        try {
            eventService.removeEvent(id);
        } catch (ServiceException e) {
            LOG.error("Cannot remove event.", e);
        }
    }

    public String updateEvent(EventDto eventDto) {
        try {
            eventService.updateEvent(eventDto);
            return "updateSuccess";
        } catch (ServiceException e) {
            LOG.error("Update failed.", e);
            return "updateFailed";
        }
    }

    public String updateEventParticipants(List<ParticipantStatusDto> statusDtos, Long eventId) {
        try {
            eventService.updateParticipantsUserStates(statusDtos, eventId);
            return "updateOfUserStatesSuccess";
        } catch (ServiceException e) {
            LOG.error("Update of userstates failed.", e);
            return "updateOfUserStatesFailed";
        }
    }

    public void changeMyStatusToEvent(Long eventId, boolean going) {
        List<ParticipantStatusDto> participants = null;
        ProfileDto currentDto;
        ParticipantStatusDto currentParticipant = null;
        try {
            participants = getParticipantsAndSatusesByEventId(eventId);
            currentDto = userController.getCurrentUser();
            participants = participants.stream()
                    .filter(participant ->
                            (!participant.getProfileDto().getEmail().equals(currentDto.getEmail())))
                    .collect(Collectors.toList());
            currentParticipant = new ParticipantStatusDto();
            currentParticipant.setProfileDto(currentDto);
            currentParticipant.setStatus(going);
            participants.add(currentParticipant);
            eventService.updateParticipantsUserStates(participants, eventId);
        } catch (ServiceException e) {
            LOG.error("Update of event participants failed.", e);
        }
    }
}
