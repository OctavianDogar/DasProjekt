package edu.msg.jbook.common.dto;

/**
 * Created by cioncag on 03.08.2016.
 */
public class CommentDto {
    private Long id;
    private Long userId;
    private String firstname;
    private String lastname;
    private String imagePath;
    private String commentText;
    private String posterMail;
    private Long parentId;

    public CommentDto() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPosterMail() {
        return posterMail;
    }

    public void setPosterMail(String posterMail) {
        this.posterMail = posterMail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        commentText = commentText.replace(":))", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/21.gif\" />");
        commentText = commentText.replace("=))", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/24.gif\" />");
        commentText = commentText.replace(":(", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/2.gif\" />");
        commentText = commentText.replace(":)", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/1.gif\" />");
        commentText = commentText.replace("=d", "<img src=\"https://s.yimg.com/pu/emoticon/v2/4.gif\" />");
        commentText = commentText.replace(":-/", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/7.gif\" />");
        commentText = commentText.replace(":-o", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/13.gif\" />");
        commentText = commentText.replace("b-)", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/16.gif\" />");
        commentText = commentText.replace("=d>", "<img src=\"https://s.yimg.com/lq/i/mesg/emoticons7/41.gif\" />");
        this.commentText = commentText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((id == null) ? 0 : id.hashCode());
        result = prime * result + Integer.parseInt(id.toString(),10);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(obj==null)
            return false;
        ProfileDto other = (ProfileDto) obj;
        if(id == null){
            if(other.getId()!=null)
                return false;
        }else{
            if(!id.equals(other.getId())){
                return false;
            }
        }
        return true;
    }
}
