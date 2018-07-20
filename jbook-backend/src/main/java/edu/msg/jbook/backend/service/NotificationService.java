package edu.msg.jbook.backend.service;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.common.dto.*;

import java.util.List;

/**
 * Created by iacobd on 08.08.2016.
 */
public interface NotificationService {
    List<NotificationDto> getNotificationsByEmail(String email) throws ServiceException;
    void setNotificationSeen(Long notificationId) throws ServiceException;

    void addNotificationByLikeDto(LikeDto dto) throws ServiceException;

    void addNotificationByCommentDto(CommentDto dto) throws ServiceException;

    void addNotificationForFriendAcceptance(ProfileDto requester, ProfileDto requestee) throws ServiceException;

    void addNotificationForEvent(PostDto postDto,Long eventId) throws ServiceException;

    void addBirthdayNotifications(ProfileDto user, ProfileDto friend) throws ServiceException;

    void addNotificationForEventChange(Event e)throws ServiceException;

    void setNotificationsForEventParticipants(ProfileDto p, Long eventId);

    void removeNotificationsForEvent(Event event);
}
