package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.EventDto;
import edu.msg.jbook.common.dto.ParticipantStatusDto;
import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.controllers.EventController;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by marcp on 01.08.2016.
 */
@ManagedBean
@RequestScoped
public class UserWallEventsBean {
    @EJB
    private UserController uc;
    @EJB
    private EventController ec;
    private List<EventDto> eventDtosParticipating = new LinkedList<>();
    private List<EventDto> eventDtosAdmining = new LinkedList<>();
    private boolean adminEventRender = false;
    private boolean participatingEventRender = false;
    private boolean noEvents = true;

    @PostConstruct
    public void init() {
        eventDtosParticipating = ec.getEventsByParticipant(uc.getCurrentUser());
        eventDtosAdmining = ec.getEventsByAdmin(uc.getCurrentUser().getEmail());

        if(eventDtosParticipating.size() > 0){
            participatingEventRender = true;
            noEvents = false;
        }
        if(eventDtosAdmining.size() > 0){
            adminEventRender = true;
            noEvents = false;
        }
    }

    public List<EventDto> getEventDtosParticipating() {
        return eventDtosParticipating;
    }

    public void setEventDtosParticipating(List<EventDto> eventDtosParticipating) {
        this.eventDtosParticipating = eventDtosParticipating;
    }

    public List<EventDto> getEventDtosAdmining() {
        return eventDtosAdmining;
    }

    public void setEventDtosAdmining(List<EventDto> eventDtosAdmining) {
        this.eventDtosAdmining = eventDtosAdmining;
    }

    public boolean isAdminEventRender() {
        return adminEventRender;
    }

    public void setAdminEventRender(boolean adminEventRender) {
        this.adminEventRender = adminEventRender;
    }

    public boolean isParticipatingEventRender() {
        return participatingEventRender;
    }

    public void setParticipatingEventRender(boolean participatingEventRender) {
        this.participatingEventRender = participatingEventRender;
    }

    public boolean isNoEvents() {
        return noEvents;
    }

    public void setNoEvents(boolean noEvents) {
        this.noEvents = noEvents;
    }
}
