package edu.msg.jbook.backend.service.beans;

import edu.msg.jbook.backend.assembler.NewsAssembler;
import edu.msg.jbook.backend.assembler.ProfileAssembler;
import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.UserStateRepository;
import edu.msg.jbook.backend.service.EventService;
import edu.msg.jbook.backend.service.NewsService;
import edu.msg.jbook.backend.service.PostService;
import edu.msg.jbook.backend.service.UserService;
import edu.msg.jbook.common.dto.*;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by iacobd on 09.08.2016.
 */
@Stateless
@DependsOn({"EventServices"})
public class NewsServiceBean implements NewsService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceBean.class);
    @EJB
    private EventService eventService;
    @EJB
    private PostService postService;
    @EJB
    private NewsAssembler na;
    @EJB
    private UserService userService;
    @EJB
    private ProfileAssembler profileAssembler;
    @EJB
    private UserStateRepository userStateRepository;

    @Override
    public List<NewsDto> getNewsForUser(String mail, int offset, int cap) {
        List<NewsDto> newsDtos = null;
        ProfileDto currentUser;
        List<EventDto> eventDtos = null;
        List<PostDto> postDtos = null;
        List<PostDto> filteredPostsByFriendship = null;
        try {
            currentUser = userService.getUserByEmail(mail);
            eventDtos = getUserFriendsEvents(currentUser);
            postDtos = postService.getAllPosts();
            filteredPostsByFriendship = postDtos.stream()
                    .filter(postDto ->
                            (userService.isFriend(postDto.getOwnerId(), currentUser.getId())))
                    .collect(Collectors.toList());
            filteredPostsByFriendship.addAll(postDtos.stream()
                    .filter(dto -> dto.getOwnerId().equals(currentUser.getId()))
                    .collect(Collectors.toList()));
            newsDtos = eventDtos.stream().map(eventDto ->
                    na.eventToNews(eventDto))
                    .collect(Collectors.toList());
            newsDtos.addAll(filteredPostsByFriendship.stream()
                    .map(postDto -> na.postToNews(postDto))
                    .collect(Collectors.toList()));
            int top = cap;
            if (cap > newsDtos.size()) {
                top = newsDtos.size();
            }
            return newsDtos.stream()
                    .sorted((o1, o2) -> o2.getCreationTime()
                            .compareTo(o1.getCreationTime()))
                    .collect(Collectors.toList()).subList(offset, top);
        } catch (ServiceException | CommonException e) {
            LOG.error("Failed to get news for user.", e);
            throw new ServiceException("Failed to get news for user.", e);
        }
    }

    public List<EventDto> getUserFriendsEvents(ProfileDto profileDto) {
        List<EventDto> resultList = null;
        UserState currentUserUserState = null;
        List<EventDto> eventDtos = null;
        List<UserEventDto> participantsDtos = null;
        try{
            resultList = new ArrayList<>();
            currentUserUserState = profileAssembler.dtoToModel(profileDto);
            eventDtos = eventService.getAllEvents();
            for (EventDto dto : eventDtos) {
                participantsDtos = eventService.getParticipants(dto);
                if (participantsDtos == null || participantsDtos.size() == 0) continue;
                for (UserEventDto participantDto : participantsDtos) {
                    UserState currentParticipant =
                            profileAssembler.dtoToModel(userService.getUserById(participantDto.getUserStateId()));
                    if (currentUserUserState.getId().equals(currentParticipant.getId())) continue;
                    if (userService.isFriend(currentUserUserState, currentParticipant)) {
                        dto.setText(currentParticipant.getUser().getFirstName() + " " + currentParticipant.getUser()
                                .getLastName() + " is attending to the event");
                        dto.setAdmin(profileAssembler.modelToDto(currentParticipant));
                        resultList.add(dto);
                        break;
                    }
                }
            }
            return resultList;
        }catch(ServiceException | CommonException e){
            LOG.error("Cannot get events to which friends are attending .",e);
            throw new ServiceException("Cannot get events to which friends are attending.",e);
        }
    }
}
