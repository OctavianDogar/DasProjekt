package edu.msg.jbook.web.controllers;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Notification;
import edu.msg.jbook.backend.service.NotificationService;
import edu.msg.jbook.common.dto.NotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by marcp on 08.08.2016.
 */
@ManagedBean
@Stateless
@DependsOn({"NotificationService"})
public class NotificationController {
    private final Logger LOG = LoggerFactory.getLogger(Notification.class);
    @EJB
    NotificationService notificationService;
    @EJB
    UserController userController;

    public List<NotificationDto> getNotifications(){
        List<NotificationDto> notificationDtos = null;
        try {
            notificationDtos = notificationService.getNotificationsByEmail(userController.getCurrentUser().getEmail());
        }catch (ServiceException e){
            LOG.error("Cannot get notifications.",e);
        }
        return notificationDtos;
    }

    public void markNotificationAsSeen(NotificationDto notificationDto) {
        try {
            notificationService.setNotificationSeen(notificationDto.getId());
        }catch (ServiceException e){
            LOG.error("Cannot mark notifications as seen.");
        }
    }

}
