package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.CreateEventDto;
import edu.msg.jbook.common.dto.EventDto;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.web.controllers.EventController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * Created by dogaro on 01/08/2016.
 */
@ManagedBean
@RequestScoped
public class EventBean implements Serializable{

    private Date eventDate = new Date();
    private String eventTime = "";
    private CreateEventDto eventDto = new CreateEventDto();
    @EJB
    private EventController ec;

    public void createEvent() {
        LocalDate eventLocalDate = new java.sql.Date(eventDate.getTime()).toLocalDate();
        int hours = Integer.parseInt(eventTime);
        LocalTime eventLocalTime = LocalTime.of(hours, 0);
        LocalDateTime localDateTime = LocalDateTime.of(eventLocalDate, eventLocalTime);
        eventDto.setDateTime(localDateTime);
        ec.createEvent(eventDto);
        try {
            FacesContext.getCurrentInstance().getExternalContext().
                    redirect("home.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public CreateEventDto getEventDto() {
        return eventDto;
    }

    public void setEventDto(CreateEventDto eventDto) {
        this.eventDto = eventDto;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
