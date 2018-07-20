package edu.msg.jbook.common.dto;

/**
 * Created by dogaro on 01/08/2016.
 */
public class UserEventDto {

    private Long id;
    public Long eventId;
    public Long userStateId;
    public boolean status;

    public UserEventDto(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserStateId() {
        return userStateId;
    }

    public void setUserStateId(Long userStateId) {
        this.userStateId = userStateId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
