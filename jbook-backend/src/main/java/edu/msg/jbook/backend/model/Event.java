package edu.msg.jbook.backend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by marcp on 27.07.2016.
 */
@Entity
@Table(name = "event")
public class Event extends BaseEntity implements Serializable {

    private String text;
    private UserState userAdmin;
    private LocalDateTime dateTime;
    private String title;

    private LocalDateTime creationTime;

    @Column(name = "title", nullable = false, length= 50)
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "text", nullable = false, length = 100)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @OneToOne
    @JoinColumn(name = "userAdmin", nullable = false)
    public UserState getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(UserState userAdmin) {
        this.userAdmin = userAdmin;
    }

    @Column(name = "date", nullable = false)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "creationTime")
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
