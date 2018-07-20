package edu.msg.jbook.backend.service.beans;

import edu.msg.jbook.backend.assembler.*;
import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.Notification;
import edu.msg.jbook.backend.model.Post;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.EventRepository;
import edu.msg.jbook.backend.repository.NotificationRepository;
import edu.msg.jbook.backend.repository.PostRepository;
import edu.msg.jbook.backend.repository.UserStateRepository;
import edu.msg.jbook.backend.service.EventService;
import edu.msg.jbook.backend.service.NotificationService;
import edu.msg.jbook.backend.service.PostService;
import edu.msg.jbook.common.NotificationType;
import edu.msg.jbook.common.dto.*;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by iacobd on 08.08.2016.
 */
@Stateless
@DependsOn({"NotificationRepository"})
public class NotificationServiceBean implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(EventServiceBean.class);
    @EJB
    UserStateRepository userStateRepository;
    @EJB
    PostRepository postRepository;
    @EJB
    EventRepository eventRepository;
    @EJB
    NotificationRepository notificationRepository;
    @EJB
    NotificationAssembler notificationAssembler;
    @EJB
    EventAssembler eventAssembler;
    @EJB
    EventService eventService;
    @EJB
    PostAssembler postAssembler;
    @EJB
    PostService postService;
    @EJB
    CommentAssembler commentAssembler;
    @EJB
    ProfileAssembler profileAssembler;

    @Override
    public List<NotificationDto> getNotificationsByEmail(String email) {
        UserState user = null;
        List<Notification> notifications = null;
        List<NotificationDto> notificationDtos = null;
        try {
            user = userStateRepository.getUserByEmail(email);
            notifications = notificationRepository.getNotificationsForUser(user);
            notificationDtos = notifications.stream().map(notif -> notificationAssembler.modelToDto(notif)).collect(Collectors.toList());
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get notifications by user failed.",e);
            throw new ServiceException("Get notifications by user failed.", e);
        }
        return notificationDtos;
    }

    @Override
    public void setNotificationSeen(Long notificationId) {
        Notification notification = null;
        try {
            notification = notificationRepository.getById(notificationId);
            notification.setSeen(true);
            notificationRepository.merge(notification);
        } catch (RepositoryException e) {
            LOG.error("Set notification seen failed.",e);
            throw new ServiceException("Set notification seen failed.", e);
        }

    }

    @Override
    public void addNotificationByLikeDto(LikeDto dto) {
        Notification notification = null;
        Post post = null;
        try {
            notification = new Notification();
            notification.setSeen(false);
            notification.setType(NotificationType.COMMENT_OR_LIKE);
            notification.setAuthor(userStateRepository.getUserByEmail(dto.getEmail()));
            post = postRepository.getById(dto.getPostId());
            notification.setPost(post);
            notification.setTarget(post.getOwner());
            notification.setDescription(notification.getAuthor().getUser().getFirstName()+" "+notification.getAuthor().getUser().getLastName()+" liked your post.");
            notification.setTime(new Date());
            notificationRepository.save(notification);
        } catch (RepositoryException e) {
            LOG.error("Adding notification for likes failed",e);
            throw new ServiceException("Adding notification for likes failed", e);
        }
    }

    @Override
    public void addNotificationByCommentDto(CommentDto dto) {
        Notification notification = null;
        Post post = null;
        Set<CommentDto> commentDtos = null;
        CommentDto commentDto = null;
        Set<Long> users = null;
        try {
            users = new HashSet<>();
            commentDtos = new HashSet<>();
            post = postRepository.getById(dto.getParentId());
            commentDto = commentAssembler.modelToDto(post);
            dto.setParentId(post.getId());
            commentDtos.addAll(postService.getPostById(post.getId()).getComments());
            setNotificationsForPostParticipants(dto,post.getOwner().getId());
            for(CommentDto cd: commentDtos){
                users.add(cd.getUserId());
            }
            for (Long l : users) {
                    setNotificationsForPostParticipants(dto,l);
            }
        } catch (RepositoryException | CommonException e) {
            LOG.error("Adding notification for comments failed",e);
            throw new ServiceException("Adding notification for comments failed", e);
        }

    }

    @Override
    public void addNotificationForFriendAcceptance(ProfileDto requester, ProfileDto requestee) {
        Notification notification = null;
        try {
            notification = new Notification();
            notification.setSeen(false);
            notification.setType(NotificationType.FRIEND_ACCEPT);
            notification.setAuthor(userStateRepository.getUserByEmail(requester.getEmail()));
            notification.setTarget(userStateRepository.getUserByEmail(requestee.getEmail()));
            notification.setDescription(requester.getFirstName()+" "+requester.getLastName()+" accepted your friend request. Send a post to your new friend!");
            notification.setTime(new Date());
            notificationRepository.save(notification);
        } catch (RepositoryException e) {
            LOG.error("Notification for friend acceptance resulted in a failure.",e);
            throw new ServiceException("Notification for friend acceptance resulted in a failure.", e);
        }

    }

    @Override
    public void addNotificationForEvent(PostDto post, Long eventId) throws ServiceException {
        List<ProfileDto> usList = null;
        UserState u = null;
        try {
            usList = eventService.getParticipantsByEventId(eventId);
            for (ProfileDto p : usList) {
                setNotificationsForEventParticipants(post, eventId, p.getEmail());
            }
            u = eventRepository.getAdminByEvent(eventId);
            if(!usList.contains(profileAssembler.modelToDto(u))) {
                setNotificationsForEventParticipants(post, eventId, u.getEmail());
            }
        } catch (RepositoryException | ServiceException | CommonException e) {
            LOG.error("Notification for events failed.",e);
            throw new ServiceException("Notification for events failed.",e);
        }
    }

    @Override
    public void addBirthdayNotifications(ProfileDto user, ProfileDto friend) throws ServiceException {
        Notification notification = null;
        try {
            notification = new Notification();
            notification.setSeen(false);
            notification.setType(NotificationType.BIRTHDAY);
            notification.setAuthor(userStateRepository.getUserByEmail(user.getEmail()));
            notification.setTarget(userStateRepository.getUserByEmail(friend.getEmail()));
            notification.setDescription("Today is"+user.getFirstName()+" "+user.getLastName()+"'s birthday! Wish him a good one!");
            if(notification.getAuthor().equals(notification.getTarget())){
                notification.setDescription("We at JBook wish you a happy birthday!");
            }
            notification.setTime(new Date());
            notificationRepository.save(notification);
        } catch (RepositoryException e) {
            LOG.error("Notification for birthday resulted in a failure.",e);
            throw new ServiceException("Notification for birthday resulted in a failure.", e);
        }
    }

    @Override
    public void addNotificationForEventChange(Event e)throws ServiceException{
        List<ProfileDto> usList = null;
        try {
            usList = eventService.getParticipantsByEventId(e.getId());
            for (ProfileDto p : usList) {
                if(!profileAssembler.modelToDto(e.getUserAdmin()).equals(p)) {
                    setNotificationsForEventParticipants(e, p.getEmail());
                }
            }
        } catch (ServiceException | CommonException re) {
            LOG.error("Notification for events failed.",re);
            throw new ServiceException("Notification for events failed.",re);
        }
    }


    private void setNotificationsForEventParticipants(Event e, String email) {
        Notification notification = null;
        try {
            notification = new Notification();
            notification.setSeen(false);
            notification.setType(NotificationType.EVENT_MODIFIED);
            notification.setAuthor(e.getUserAdmin());
            notification.setEvent(e);
            notification.setDescription(e.getUserAdmin().getUser().getFirstName()+" "+e.getUserAdmin().getUser().getLastName()+" modified the event "+e.getTitle());
            notification.setTime(new Date());
            notification.setTarget(userStateRepository.getUserByEmail(email));
            if(notification.getAuthor().equals(notification.getTarget()))
                return;
            notificationRepository.save(notification);
        } catch (RepositoryException re) {
            LOG.error("Notification for events failed.",re);
            throw new ServiceException("Notification for events failed.",re);
        }
    }

    @Override
    public void setNotificationsForEventParticipants(ProfileDto p, Long eventId) {
        Notification notification = null;
        try {
            notification = new Notification();
            notification.setSeen(false);
            notification.setType(NotificationType.EVENT_IN_A_DAY);
            notification.setEvent(eventRepository.getById(eventId));
            notification.setAuthor(eventRepository.getAdminByEvent(eventId));
            notification.setDescription(notification.getEvent().getText()+" is in a day. You should prepare for it!");
            notification.setTime(new Date());
            notification.setTarget(userStateRepository.getById(p.getId()));
            notificationRepository.save(notification);
        } catch (RepositoryException e) {
            LOG.error("Notification for events failed.",e);
            throw new ServiceException("Notification for events failed.",e);
        }
    }

    private void setNotificationsForEventParticipants(PostDto p, Long eventId,String email) {
        Notification notification = null;
        try {
            notification = new Notification();
            notification.setSeen(false);
            notification.setType(NotificationType.POST);
            notification.setEvent(eventRepository.getById(eventId));
            notification.setAuthor(userStateRepository.getById(p.getOwnerId()));
            notification.setDescription(notification.getAuthor().getUser().getFirstName()+" "+notification.getAuthor().getUser().getLastName()+" posted on the event "+eventRepository.getById(eventId).getTitle()+".");
            notification.setTime(new Date());
            notification.setTarget(userStateRepository.getUserByEmail(email));
            if(notification.getAuthor().equals(notification.getTarget())){
                return;
            }
            notificationRepository.save(notification);
        } catch (RepositoryException e) {
            LOG.error("Notification for events failed.",e);
            throw new ServiceException("Notification for events failed.",e);
        }
    }



    private void setNotificationsForPostParticipants(CommentDto source,Long targetId) {
        Notification notification = null;
        try {
            notification = new Notification();
            notification.setSeen(false);
            notification.setType(NotificationType.COMMENT_OR_LIKE);
            notification.setAuthor(userStateRepository.getById(source.getUserId()));
            notification.setDescription(notification.getAuthor().getUser().getFirstName()+" "+notification.getAuthor().getUser().getLastName()+" commented on the post.");
            notification.setTime(new Date());
            notification.setTarget(userStateRepository.getById(targetId));
            notification.setPost(postRepository.getById(source.getParentId()));
            if(notification.getAuthor().equals(notification.getTarget()))
                return;
            notificationRepository.save(notification);
        } catch (RepositoryException e) {
            LOG.error("Notification for posts failed.",e);
            throw new ServiceException("Notification for posts failed.",e);
        }
    }

    @Override
    public void removeNotificationsForEvent(Event event) {
        try {
            notificationRepository.removeNotificationsForEvent(event);
        } catch(RepositoryException e) {
            LOG.error("Cannot remove notifications for event.",e);
            throw new ServiceException("Cannot remove notifications for event.",e);
        }
    }

}
