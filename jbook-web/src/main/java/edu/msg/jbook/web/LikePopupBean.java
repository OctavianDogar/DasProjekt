package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.controllers.PostController;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iacobd on 03.08.2016.
 */


@ManagedBean
@javax.faces.bean.SessionScoped
public class LikePopupBean implements Serializable {
    private Long postId;
    private List<SearchDto> currentPostLikes;
    @EJB
    private PostController postController;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long post) {
        this.postId = post;
    }

    public void userSelectedPopup(SearchDto user) {
        RequestContext.getCurrentInstance().closeDialog(user);
    }

    public void closeDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
    }

    public List<SearchDto> getCurrentPostLikes() {
        return currentPostLikes;
    }

    public void setCurrentPostLikes(List<SearchDto> currentPostLikes) {
        this.currentPostLikes = currentPostLikes;
    }

    public void openPopUp(Long postId) {
        currentPostLikes = postController.getPostById(postId).getLikes();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("resizable", false);
        options.put("draggable", false);
        RequestContext.getCurrentInstance().openDialog("like_popup", options, null);
    }
}