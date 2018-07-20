package edu.msg.jbook.backend.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ilyesk on 27.07.2016.
 */
@Entity
@Table(name="post")
public class Post extends BaseEntity {
    UserState owner;
    Privacy privacy;
    Event event;
    Post parentOfComment;
    String photo;
    String video;
    String text;
    List<UserState> userLikes;
    LocalDateTime creationTime;

    @ManyToOne(optional=false)
    @JoinColumn(name="ownerId", nullable=false)
    public UserState getOwner() {
        return owner;
    }

    public void setOwner(UserState owner) {
        this.owner = owner;
    }

    @Column(name="privacy",nullable=true)
    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    @ManyToOne(optional=true)
    @JoinColumn(name="eventId",nullable=true)
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    @OneToOne
    @JoinColumn(name="parent")
    public Post getParentOfComment() {
        return parentOfComment;
    }

    public void setParentOfComment(Post parentOfComment) {
        this.parentOfComment = parentOfComment;
    }

    @Column(name="photo")
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Column(name="video")
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Column(name="text",length = 300)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ManyToMany
    @JoinTable(name="userLikes")
    public List<UserState> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(List<UserState> userLikes) {
        this.userLikes = userLikes;
    }

    @Column(name = "creationTime")
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

}
