package edu.msg.jbook.common.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by cioncag on 29.07.2016.
 */
public class PostDto {

    private String teporaryComment;
    private Long id;
    private Long ownerId;
    private String email;
    private String firstName;
    private String lastName;
    private List<CommentDto> comments;
    private Privacy privacy;
    private String userPhoto = "";
    private String photo = "";
    private String video = "";
    private String text = "";
    private boolean renderText;
    private boolean renderImage;
    private boolean renderVideo;
    private String numberOfLikes;
    private LocalDateTime creationTime;
    private List<SearchDto> likes;
    private Long eventId;

    public PostDto() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTeporaryComment() {
        return teporaryComment;
    }

    public void setTeporaryComment(String teporaryComment) {
        this.teporaryComment = teporaryComment;
    }

    public List<SearchDto> getLikes() {
        return likes;
    }

    public void setLikes(List<SearchDto> likes) {
        numberOfLikes = String.valueOf(likes.size());
        this.likes = likes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        text = text.replace(":))", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/21.gif\" />");
        text = text.replace("=))", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/24.gif\" />");
        text = text.replace(":(", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/2.gif\" />");
        text = text.replace(":)", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/1.gif\" />");
        text = text.replace("=d", "<img src=\"https://s.yimg.com/pu/emoticon/v2/4.gif\" />");
        text = text.replace(":-/", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/7.gif\" />");
        text = text.replace(":-o", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/13.gif\" />");
        text = text.replace("b-)", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/16.gif\" />");
        text = text.replace("=d>", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/41.gif\" />");

        this.text = text;
    }

    public String getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(String numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public boolean isRenderText() {
        return renderText;
    }

    public void setRenderText(boolean renderText) {
        this.renderText = renderText;
    }

    public boolean isRenderImage() {
        return renderImage;
    }

    public void setRenderImage(boolean renderImage) {
        this.renderImage = renderImage;
    }

    public boolean isRenderVideo() {
        return renderVideo;
    }

    public void setRenderVideo(boolean renderVideo) {
        this.renderVideo = renderVideo;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public enum Privacy {
        ONLY_ME, FRIENDS, FRIENDS_OF_FRIENDS, PUBLIC;
    }

}
