package edu.msg.jbook.web;

import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.common.dto.EventDto;
import edu.msg.jbook.common.dto.ParticipantStatusDto;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.web.controllers.EventController;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dogaro on 11/08/2016.
 */
@ManagedBean
@ViewScoped
public class UserWallEventUpdateBean {
    @EJB
    private UserController uc;
    @EJB
    private EventController ec;

    private EventDto currentEvent = new EventDto();
    private List<ParticipantStatusDto> participantStatuses;
    private boolean noParticipantsFlag = true;

    @PostConstruct
    public void init() {
        Long editedEventId = new Long(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap()
                .get("eventId"));
        currentEvent = ec.getEventById(editedEventId);

                participantStatuses = ec.getParticipantsAndSatusesByEventId(editedEventId);
        if(participantStatuses.size()==0 || participantStatuses==null){
            noParticipantsFlag = false;
        }
    }

    public void updateEvent(){
        String result = ec.updateEvent(currentEvent);
        if(result.equals("updateSuccess")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Event updated successfully"));
        }
        ec.updateEventParticipants(participantStatuses, currentEvent.getId());
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("eventLayout.xhtml?faces-redirect=true&includeViewParams=true&eventId="+currentEvent
                            .getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EventDto getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(EventDto currentEvent) {
        this.currentEvent = currentEvent;
    }

    public List<ParticipantStatusDto> getParticipantStatuses() {
        return participantStatuses;
    }

    public void setParticipantStatuses(List<ParticipantStatusDto> participantStatuses) {
        this.participantStatuses = participantStatuses;
    }

    public boolean isNoParticipantsFlag() {
        return noParticipantsFlag;
    }

    public void setNoParticipantsFlag(boolean noParticipantsFlag) {
        this.noParticipantsFlag = noParticipantsFlag;
    }

}

