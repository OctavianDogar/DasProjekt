package edu.msg.jbook.backend.service;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.common.dto.*;

import java.util.List;

/**
 * Created by dogaro on 29/07/2016.
 */
public interface EventService {

    EventDto getEventById(Long id) throws ServiceException;

    EventDto getEventByAdmin(String email) throws ServiceException;

    Event insertEvent(CreateEventDto dto) throws ServiceException;

    void updateEvent(EventDto dto) throws ServiceException;

    List<EventDto> getEventsByAdmin(String email) throws ServiceException;

    List<UserEventDto> getParticipants(EventDto eventDto) throws ServiceException;

    List<EventDto> getEventsByParticipant(ProfileDto profileDto) throws ServiceException;

    List<EventDto> getEventsByEmail(String email) throws ServiceException;

    List<ProfileDto> getParticipantsByEventId(Long id) throws ServiceException;

    List<PostDto> getEventPostsByEventId(Long id) throws ServiceException;

    List<UserEventDto> getUserEventsByEventId(Long id);

    List<ParticipantStatusDto> getParticipantsAndSatusesByEventId(Long id);

    List<EventDto> getAllEvents() throws ServiceException;

    Integer participantsNumber(Long id) throws ServiceException;

    void removeEvent(Long id) throws ServiceException;

    void updateParticipantsUserStates(List<ParticipantStatusDto> dtosList, Long id) throws ServiceException;
}
