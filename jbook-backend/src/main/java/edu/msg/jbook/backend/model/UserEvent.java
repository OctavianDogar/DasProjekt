package edu.msg.jbook.backend.model;

import javax.persistence.*;

/**
 * Created by cioncag on 27.07.2016.
 */

@Entity
@Table(
        name="userEvent",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"userState", "event"})
)
public class UserEvent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Event event;

    private UserState userState;

    private boolean status;

    @Column(name = "status", nullable = false)
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "event")
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @ManyToOne
    @JoinColumn(name = "userState")
    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}