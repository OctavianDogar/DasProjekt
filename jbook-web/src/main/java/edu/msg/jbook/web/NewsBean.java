package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.NewsDto;
import edu.msg.jbook.common.dto.PostDto;
import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.controllers.NewsController;
import edu.msg.jbook.web.controllers.PostController;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by dogaro on 02/08/2016.
 */
@ManagedBean(name = "newsBean")
@ViewScoped
public class NewsBean implements Serializable {

    @EJB
    private NewsController newsController;
    @EJB
    private PostController postController;
    private int offsetIndex = 0;
    private List<NewsDto> newsDtos;
    private boolean render = false;
    private int chunkSize;

    public void updateNews() {
        render = true;
        chunkSize = 10;
        offsetIndex = 10;
        newsDtos = newsController.getNews(0, offsetIndex);
    }

    @PostConstruct
    public void init() {
        render = true;
        chunkSize = 10;
        offsetIndex = 10;
        newsDtos = newsController.getNews(0, offsetIndex);
    }

    public void addLikeToPost(Long postId) {
        postController.addLikeToPost(postId);
        PostDto post = postController.getPostById(postId);
        for (NewsDto news : newsDtos) {
            if (news.isPostOrEvent()) {
                if (news.getPostDto().getId() == postId) {
                    news.getPostDto().setLikes(post.getLikes());
                }
            }
        }
    }

    public void addComment(PostDto post) {
        postController.addCommentToPost(post.getId(), post.getTeporaryComment());
        post.setTeporaryComment("");
        PostDto updatedPost = postController.getPostById(post.getId());
        for (NewsDto news : newsDtos) {
            if (news.isPostOrEvent()) {
                if (news.getPostDto().getId() == post.getId()) {
                    news.getPostDto().setComments(updatedPost.getComments());
                }
            }

        }
    }

    public void onUserChoosen(SelectEvent event) {
        SearchDto dto = (SearchDto) event.getObject();
        try {
            if (dto == null) {
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect("home.xhtml");
            }
            else{
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect("friend_wall.xhtml?faces-redirect=true&includeViewParams=true&id=" + dto.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public List<NewsDto> getNewsDtos() {
        return newsDtos;
    }

    public void setNewsDtos(List<NewsDto> newsDtos) {
        this.newsDtos = newsDtos;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public String doGoToUser(Long id) {
        return "friend_wall.xhtml?faces-redirect=true&includeViewParams=true&id=" + id;
    }

}
