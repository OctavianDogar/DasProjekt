package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.PostDto;
import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.controllers.PostController;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by iacobd on 08.08.2016.
 */
@ManagedBean
@ViewScoped
public class PostDetailsBean implements Serializable {

    private PostDto postDto;
    private String comment;
    @EJB
    private PostController postController;

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @PostConstruct
    public void init() {
        Long postId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        if (postId != null) {
            postDto = postController.getPostById(postId);
        }
    }

    public PostDto getPostDto() {
        return postDto;
    }

    public void setPostDto(PostDto postDto) {
        this.postDto = postDto;
    }

    public void addLikeToPost() {
        postController.addLikeToPost(postDto.getId());
        PostDto post = postController.getPostById(postDto.getId());
        postDto.setLikes(post.getLikes());
    }


    public void addComment() {
        postController.addCommentToPost(postDto.getId(), comment);
        comment = "";
        PostDto updatedPost = postController.getPostById(postDto.getId());
        postDto.setComments(updatedPost.getComments());
    }

    public void onUserChoosen(SelectEvent event) {
        SearchDto dto = (SearchDto) event.getObject();
        try {
            if (dto == null) {
                Long postId = postDto == null ? null : postDto.getId();
                if (postId != null) {
                    FacesContext.getCurrentInstance().getExternalContext().
                            redirect("post_details.xhtml?faces-redirect=true&includeViewParams=true&id=" + postId);
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect("friend_wall.xhtml?faces-redirect=true&includeViewParams=true&id=" + dto.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
}
