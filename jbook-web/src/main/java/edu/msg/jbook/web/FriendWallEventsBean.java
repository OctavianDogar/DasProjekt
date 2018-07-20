package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.EventDto;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.web.controllers.EventController;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ilyesk on 03.08.2016.
 */
@ManagedBean
@RequestScoped
public class FriendWallEventsBean {
    @EJB
    private EventController ec;
    @EJB
    private UserController uc;
    private ProfileDto user = new ProfileDto();
    private Boolean eventRender = false;
    private boolean noEvents = false;
    private Set<EventDto> eventDtos = new HashSet<>();

    @PostConstruct
    public void getEventsForFriend() {
        String query = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (query != null && !query.equals("")) {
            user = uc.getFriendById(Long.parseLong(query));
            List<EventDto> temp= ec.getEventsByParticipant(user);
            eventDtos.addAll(temp);
            eventDtos.addAll(ec.getEventsByAdmin(user.getEmail()));
            if(eventDtos.size() > 0){
                eventRender = true;
                noEvents = false;
            }else{
                eventRender = false;
                noEvents = true;
            }
        }
    }

    public ProfileDto getUser() {
        return user;
    }

    public void setUser(ProfileDto user) {
        this.user = user;
    }

    public Boolean getEventRender() {
        return eventRender;
    }

    public void setEventRender(Boolean eventRender) {
        this.eventRender = eventRender;
    }

    public Set<EventDto> getEventDtos() {
        return eventDtos;
    }

    public void setEventDtos(Set<EventDto> eventDtos) {
        this.eventDtos = eventDtos;
    }

    public boolean isNoEvents() {
        return noEvents;
    }

    public void setNoEvents(boolean noEvents) {
        this.noEvents = noEvents;
    }
}
