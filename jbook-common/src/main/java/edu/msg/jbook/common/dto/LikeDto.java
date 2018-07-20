package edu.msg.jbook.common.dto;

/**
 * Created by iacobd on 03.08.2016.
 */
public class LikeDto {

    private Long postId;
    private String email;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
