package edu.msg.jbook.common.dto;

import edu.msg.jbook.common.NotificationType;

import java.util.Date;

/**
 * Created by marcp on 08.08.2016.
 */
public class NotificationDto {

    private Long id;
    private Long typeId;
    private NotificationType type;
    private String message;
    private SearchDto author;
    private Boolean seen;
    private Date date;
    private String eventName;

    public NotificationDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public SearchDto getAuthor() {
        return author;
    }

    public void setAuthor(SearchDto author) {
        this.author = author;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
