package edu.msg.jbook.backend.service.beans;

import edu.msg.jbook.backend.assembler.*;
import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.Post;
import edu.msg.jbook.backend.model.UserEvent;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.EventRepository;
import edu.msg.jbook.backend.repository.PostRepository;
import edu.msg.jbook.backend.repository.UserEventRepository;
import edu.msg.jbook.backend.repository.UserStateRepository;
import edu.msg.jbook.backend.service.EventService;
import edu.msg.jbook.backend.service.NotificationService;
import edu.msg.jbook.backend.service.PostService;
import edu.msg.jbook.common.dto.*;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dogaro on 01/08/2016.
 */
@Stateless
@DependsOn({"EventRepository"})
public class EventServiceBean implements EventService {
    private static final Logger LOG = LoggerFactory.getLogger(EventServiceBean.class);
    @EJB
    private EventRepository eventRepository;
    @EJB
    private UserEventRepository userEventRepository;
    @EJB
    private PostRepository postRepository;
    @EJB
    private PostService postService;
    @EJB
    private UserStateRepository userStateRepository;
    @EJB
    private UserEventAssembler userEventAssembler;
    @EJB
    private EventAssembler eventAssembler;
    @EJB
    private CreateEventAssembler createEventAssembler;
    @EJB
    private ProfileAssembler profileAssembler;
    @EJB
    private PostAssembler postAssembler;
    @EJB
    private EventService eventService;
    @EJB
    private NotificationService notificationService;

    @Override
    public EventDto getEventById(Long id) throws ServiceException {
        Event event = null;
        try {
            event = eventRepository.getById(id);
            EventDto dto = eventAssembler.modelToDto(event);
            return dto;
        } catch (RepositoryException |CommonException e) {
            LOG.error("Get event by id failed",e);
            throw new ServiceException("Get event by id failed",e);
        }
    }

    @Override
    public EventDto getEventByAdmin(String email) throws ServiceException {
        Event event = null;
        List<Event> events = null;
        try {
            events = eventRepository.getEventsByAdmin(email);
            event = events.get(0);
            return eventAssembler.modelToDto(event);
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get event by admin failed",e);
            throw new ServiceException("Get event by admin failed",e);
        }
    }

    @Override
    public Event insertEvent(CreateEventDto dto) throws ServiceException {
        Event event = null;
        UserState userState = null;
        try {
            event = createEventAssembler.dtoToModel(dto);
            event.setCreationTime(LocalDateTime.now());
            userState = userStateRepository.getUserByEmail(dto.getAdminEmail());
            event.setUserAdmin(userState);
            return eventRepository.save(event);
        } catch (RepositoryException | CommonException e) {
            LOG.error("Inserting event failed",e);
            throw new ServiceException("Inserting event failed",e);
        }
    }

    @Override
    public void updateEvent(EventDto dto) throws ServiceException {
        Event got = null;
        Event deAssembled = null;
        try {
            deAssembled = eventAssembler.dtoToModel(dto);
            got = eventRepository.getById(dto.getId());
            if (!deAssembled.getText().equals(got.getText())) {
                got.setText(deAssembled.getText());
            }
            if (!deAssembled.getUserAdmin().equals(got.getUserAdmin())) {
                got.setUserAdmin(deAssembled.getUserAdmin());
            }
            if (!deAssembled.getDateTime().equals(got.getDateTime())) {
                got.setDateTime(deAssembled.getDateTime());
            }
            if (!deAssembled.getTitle().equals(got.getTitle())) {
                got.setTitle(deAssembled.getTitle());
            }
            notificationService.addNotificationForEventChange(got);
            eventRepository.merge(got);
        } catch (RepositoryException | CommonException e) {
            LOG.error("Updating event failed",e);
            throw new ServiceException("Updating event failed",e);
        }

    }

    @Override
    public List<EventDto> getEventsByAdmin(String email) throws ServiceException {
        List<EventDto> eventDtos = null;
        List<Event> events = null;
        try {
            events = eventRepository.getEventsByAdmin(email);
            eventDtos = events.stream().map(event1 -> eventAssembler.modelToDto(event1)).collect(Collectors.toList());
            return eventDtos;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get events by admin failed.", e);
            throw new ServiceException("Get events by admin failed.", e);
        }
    }

    @Override
    public List<UserEventDto> getParticipants(EventDto eventDto) throws ServiceException {
        List<UserEventDto> userEventDtos = null;
        Event event = null;
        List<UserEvent> userEvents = null;
        try {
            event = eventRepository.getById(eventDto.getId());
            userEvents = userEventRepository.getUserEventsByEvent(event);
            userEventDtos = userEvents.stream().
                    map(userEvent -> userEventAssembler.modelToDto(userEvent)).collect(Collectors.toList());
            return userEventDtos;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get participants by admin failed.", e);
            throw new ServiceException("Get events by admin failed.", e);
        }
    }

    @Override
    public List<EventDto> getEventsByParticipant(ProfileDto profileDto) throws ServiceException {
        List<UserEvent> userEvents = null;
        List<EventDto> eventDtos = null;
        UserState state = null;
        try {
            eventDtos = new ArrayList<>();
            state = profileAssembler.dtoToModel(profileDto);
            userEvents = userEventRepository.getUserEventsByUser(state);
            for (UserEvent ue : userEvents) {
                eventDtos.add(eventAssembler.modelToDto(ue.getEvent()));
            }
            return eventDtos;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get events by participant failed.", e);
            throw new ServiceException("Get events by participant failed.", e);
        }
    }

    @Override
    public List<EventDto> getEventsByEmail(String email) throws ServiceException {
        List<UserEvent> userEvents = null;
        List<EventDto> eventDtos = null;
        try {
            eventDtos = new ArrayList<>();
            userEvents = userEventRepository.getUserEventsByUserEmail(email);
            for (UserEvent ue : userEvents) {
                eventDtos.add(eventAssembler.modelToDto(ue.getEvent()));
            }
            return eventDtos;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get events by participant failed.", e);
            throw new ServiceException("Get events by participant failed.", e);
        }
    }

    @Override
    public List<ProfileDto> getParticipantsByEventId(Long id) throws ServiceException {
        List<UserEvent> userEvents = null;
        List<ProfileDto> participantsDtos = null;
        List<UserState> participants = null;
        Event event = null;
        try {
            event = eventRepository.getById(id);
            userEvents = userEventRepository.getUserEventsByEvent(event);
            participants = userEvents.stream()
                    .map(userEvent -> userEvent.getUserState())
                    .collect(Collectors.toList());
            participantsDtos = participants.stream()
                    .map(userState -> profileAssembler.modelToDto(userState))
                    .collect(Collectors.toList());
            return participantsDtos;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get participants by eventId failed!", e);
            throw new ServiceException("Get participants by eventId failed!", e);
        }
    }

    @Override
    public List<PostDto> getEventPostsByEventId(Long id) throws ServiceException {
        List<PostDto> postDtos = null;
        List<Post> posts = null;
        Event event = null;
        try {
            event = eventRepository.getById(id);
            posts = postRepository.getPostsByEvent(event);
            postDtos = posts.stream()
                    .map(post -> postAssembler.modelToDto(post))
                    .collect(Collectors.toList());
            return postDtos;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get posts by eventId failed!", e);
            throw new ServiceException("Get posts by eventId failed!", e);
        }
    }

    @Override
    public List<UserEventDto> getUserEventsByEventId(Long id) {
        List<UserEvent> userEvents = null;
        Event event = null;
        try {
            event = eventRepository.getById(id);
            userEvents = userEventRepository.getUserEventsByEvent(event);
            return userEvents.stream()
                    .map(userEvent -> userEventAssembler
                            .modelToDto(userEvent))
                    .collect(Collectors.toList());
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get posts by eventId failed!", e);
            throw new ServiceException("Get posts by eventId failed!", e);
        }
    }

    @Override
    public List<ParticipantStatusDto> getParticipantsAndSatusesByEventId(Long id) {
        List<UserEventDto> userEventDtos = null;
        UserState state = null;
        ProfileDto profile = null;
        List<ParticipantStatusDto> participantStatuses = null;
        try {
            participantStatuses = new ArrayList<>();
            userEventDtos = eventService.getUserEventsByEventId(id);
            for (UserEventDto ued : userEventDtos) {
                state = userStateRepository.getById(ued.getUserStateId());
                profile = profileAssembler.modelToDto(state);
                participantStatuses.add(new ParticipantStatusDto(profile, ued.isStatus()));
            }
            return participantStatuses;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get participants and statuses by id failed!", e);
            throw new ServiceException("Get posts by eventId failed!", e);
        }
    }

    @Override
    public List<EventDto> getAllEvents() throws ServiceException {
        List<Event> eventList = null;
        List<EventDto> dtoList = null;
        try {
            eventList = eventRepository.getAll();
            dtoList = new ArrayList<>();
            for (Event ev : eventList) {
                dtoList.add(eventAssembler.modelToDto(ev));
            }
            return dtoList;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Cannot get all events!", e);
            throw new ServiceException("Cannot get all events!", e);
        }
    }

    @Override
    public Integer participantsNumber(Long id) throws ServiceException {
        try {
            return userEventRepository.getUserEventsByEvent(eventRepository.getById(id)).size();
        } catch (RepositoryException e) {
            LOG.error("Cannot get participants number!", e);
            throw new ServiceException("Cannot get participants number!", e);
        }
    }

    @Override
    public void removeEvent(Long id) throws ServiceException {
        Event event = null;
        List<Post> eventsPosts = null;
        List<UserEvent> userEvents = null;
        try {
            event = eventRepository.getById(id);
            notificationService.removeNotificationsForEvent(event);
            eventsPosts = postRepository.getPostsByEvent(event);
            userEvents = userEventRepository.getUserEventsByEvent(event);
            for (Post p : eventsPosts) {
                postRepository.delete(p);
            }
            for (UserEvent ue : userEvents) {
                userEventRepository.delete(ue);
            }
            eventRepository.delete(event);
        } catch (RepositoryException |ServiceException e) {
            LOG.error("Cannot remove event!", e);
            throw new ServiceException("Cannot remove event!", e);
        }
    }

    @Override
    public void updateParticipantsUserStates(List<ParticipantStatusDto> partiStatusDto, Long eventId) throws
            ServiceException {
        Event currentEvent = null;
        List<UserEvent> userEvents = null;
        try {
            currentEvent = eventRepository.getById(eventId);
            userEvents = userEventRepository.getUserEventsByEvent(currentEvent);
            for (ParticipantStatusDto partStatDto : partiStatusDto) {
                //partStatDto contine id.urile partStrutusurilor din form; cu tot cu noile statusuri booleene
                boolean flag = false;
                for (UserEvent uev : userEvents) {
                    if (partStatDto.getProfileDto().getId().equals(uev.getUserState().getId())) {
                        uev.setStatus(partStatDto.isStatus());
                        userEventRepository.merge(uev);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    UserEvent userEvent = new UserEvent();
                    userEvent.setEvent(currentEvent);
                    userEvent.setUserState(userStateRepository.getById(partStatDto.getProfileDto().getId()));
                    userEvent.setStatus(partStatDto.isStatus());
                    userEventRepository.save(userEvent);
                }
            }
        } catch (RepositoryException e) {
            LOG.error("Cannot update userEvents from updateEventForm!", e);
            throw new ServiceException("Cannot update userEvents from updateEventForm!", e);
        }
    }
}