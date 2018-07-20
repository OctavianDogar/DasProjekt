package edu.msg.jbook.backend.model;

import edu.msg.jbook.common.NotificationType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by iacobd on 27.07.2016.
 */
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    private String description;
    private NotificationType type;
    private Post post;
    private Event event;
    private UserState target;
    private UserState author;
    private Boolean seen;
    private Date time;

    @Column(name = "description", nullable = false, length = 300)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "type", nullable = false)
    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    @JoinColumn(name = "post")
    @ManyToOne
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @JoinColumn(name = "event")
    @ManyToOne
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @ManyToOne
    public UserState getTarget() {
        return target;
    }

    public void setTarget(UserState target) {
        this.target = target;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @ManyToOne
    public UserState getAuthor() {
        return author;
    }


    public void setAuthor(UserState author) {
        this.author = author;
    }
}
