package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.PostDto;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.controllers.PostController;
import edu.msg.jbook.web.controllers.UserController;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by marcp on 01.08.2016.
 */
@ManagedBean
@RequestScoped
public class UserWallBean implements Serializable {

    private ProfileDto user = new ProfileDto();
    private List<ProfileDto> userFriends = new LinkedList<ProfileDto>();
    private UploadedFile file;
    private List<PostDto> postDtos;
    @EJB
    private UserController uc;
    @EJB
    private PostController pc;
    private boolean postRender = false;
    private boolean friendsRender = false;
    private boolean noFriends = false;
    private boolean noPosts = false;
    private String link;

    @PostConstruct
    public void initWall() {
        user = uc.getCurrentUser();
        if (user != null) {
            userFriends = uc.getUserFriends(user.getEmail());
            postDtos = pc.getPostsByUser(user.getEmail());
            if (userFriends.size() > 0) {
                friendsRender = true;
                noFriends = false;
            } else {
                friendsRender = false;
                noFriends = true;
            }
            if (postDtos.size() > 0) {
                postRender = true;
                noPosts = false;
            } else {
                postRender = false;
                noPosts = true;
            }
        }
    }

    public void updateProfile() {
        uc.updateProfile(user);
    }

    public ProfileDto getUser() {
        return user;
    }

    public void setUser(ProfileDto user) {
        this.user = user;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<PostDto> getPostDtos() {
        return postDtos;
    }

    public void setPostDtos(List<PostDto> postDtos) {
        this.postDtos = postDtos;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<ProfileDto> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(List<ProfileDto> userFriends) {
        this.userFriends = userFriends;
    }

    public boolean isFriendsRender() {
        return friendsRender;
    }

    public void setFriendsRender(boolean friendsRender) {
        this.friendsRender = friendsRender;
    }

    public boolean isPostRender() {
        return postRender;
    }

    public void setPostRender(boolean postRender) {
        this.postRender = postRender;
    }

    public boolean isNoFriends() {
        return noFriends;
    }

    public void setNoFriends(boolean noFriends) {
        this.noFriends = noFriends;
    }

    public boolean isNoPosts() {
        return noPosts;
    }

    public void setNoPosts(boolean noPosts) {
        this.noPosts = noPosts;
    }

    public void upload() {
        ImageUploadDropboxBean.fileUpload = this.file;
        String imagePath = "";
        try {
            imagePath = ImageUploadDropboxBean.uploadImage();
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            user.setPhoto(imagePath);
        } catch (IOException e) {
        }
        HttpSession session = (HttpSession) FacesContext
                .getCurrentInstance().getExternalContext()
                .getSession(false);
        String email = (String) session.getAttribute("email");
        uc.updateUserPhoto(email, imagePath);
    }

    public void deleteFriend(ProfileDto friend) {
        uc.removeFriend(friend);
        userFriends.remove(friend);
        if (userFriends.size() == 0) {
            friendsRender = false;
            noFriends = true;
        }
    }

    public String getEvents() {
        return "user_wall_events.xhtml";
    }

    public String getRequests() {
        return "user_wall_requests.xhtml";
    }

    public String getPosts() {
        return "user_wall.xhtml";
    }

    public void addLikeToPost(Long postId) {
        pc.addLikeToPost(postId);
        PostDto post = pc.getPostById(postId);
        for (PostDto news : postDtos) {
            if (news.getId() == postId) {
                news.setLikes(post.getLikes());
            }
        }
    }

    public void addComment(PostDto post) {
        pc.addCommentToPost(post.getId(), post.getTeporaryComment());
        post.setTeporaryComment("");
        PostDto updatedPost = pc.getPostById(post.getId());
        for (PostDto news : postDtos) {
            if (news.getId() == post.getId()) {
                news.setComments(updatedPost.getComments());
            }
        }
    }

    public void onUserChoosen(SelectEvent event) {
        SearchDto dto = (SearchDto) event.getObject();
        try {
            if (dto == null) {
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect("user_wall.xhtml");
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
