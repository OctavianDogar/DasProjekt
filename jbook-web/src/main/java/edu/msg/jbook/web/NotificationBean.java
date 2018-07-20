package edu.msg.jbook.web;

import edu.msg.jbook.common.NotificationType;
import edu.msg.jbook.common.dto.NotificationDto;
import edu.msg.jbook.web.controllers.NotificationController;
import edu.msg.jbook.web.language.bean.LanguageBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by marcp on 08.08.2016.
 */
@ManagedBean
@RequestScoped
public class NotificationBean {

    @EJB
    NotificationController notificationController;
    private List<NotificationDto> notificationDtos = new LinkedList<NotificationDto>();
    private boolean render = false;
    private boolean noNotifications = false;
    private int notificationSize = 0;
    private int numberOfUnseen;

    @PostConstruct
    public void init(){

        notificationDtos = notificationController.getNotifications();
        notificationSize = notificationDtos.size();



        if (notificationDtos.size() > 0) {
            render = true;
            noNotifications = false;
        } else {
            render = false;
            noNotifications = true;
        }
        for(NotificationDto notificationDto : notificationDtos) {
            if(!notificationDto.getSeen()) {
                numberOfUnseen++;
            }
        }

    }


    public String getNotificationDate(NotificationDto notificationDto) {
        String stringDate = "";
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        stringDate = df.format(notificationDto.getDate());
        return stringDate;
    }

    public void updateNotifications() {
        notificationDtos = notificationController.getNotifications();
        notificationSize = notificationDtos.size();
        numberOfUnseen = 0;
        for(NotificationDto notificationDto : notificationDtos) {
            if(!notificationDto.getSeen()) {
                numberOfUnseen++;
            }
        }
    }



    public void markNotificationAsSeen(NotificationDto notificationDto) {
        this.numberOfUnseen--;
        notificationDto.setSeen(true);
        notificationController.markNotificationAsSeen(notificationDto);
    }

    public List<NotificationDto> getNotificationDtos() {
        return notificationDtos;
    }

    public void setNotificationDtos(List<NotificationDto> notificationDtos) {
        this.notificationDtos = notificationDtos;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public boolean isNoNotifications() {
        return noNotifications;
    }

    public void setNoNotifications(boolean noNotifications) {
        this.noNotifications = noNotifications;
    }

    public int getNumberOfUnseen() {
        return numberOfUnseen;
    }

    public void setNumberOfUnseen(int numberOfUnseen) {
        this.numberOfUnseen = numberOfUnseen;
    }

    public int getNotificationSize() {
        return notificationSize;
    }

    public void setNotificationSize(int notificationSize) {
        this.notificationSize = notificationSize;
    }
}
