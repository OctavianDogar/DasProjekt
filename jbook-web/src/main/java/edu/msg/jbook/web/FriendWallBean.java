package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.PostDto;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.controllers.PostController;
import edu.msg.jbook.web.controllers.UserController;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ilyesk on 02.08.2016.
 */
@ManagedBean
@ViewScoped

public class FriendWallBean {

    @EJB
    private UserController uc;
    @EJB
    private PostController pc;
    private ProfileDto user = new ProfileDto();
    private List<PostDto> postDtos = new LinkedList<PostDto>();
    private boolean noPosts = false;
    private boolean leftRightRender = false;
    private boolean privateUser = false;
    private boolean isFriend = false;
    private boolean friendRequestSent = false;
    private boolean friendRequestButton = true;
    private boolean hasSentFriendRequest = false;
    private boolean hasReceivedFriendRequest = false;

    @PostConstruct
    public void seeFriendWall() {
        Long anotherUser = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        if (anotherUser != null) {
            if (uc.isUser(anotherUser)) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().
                            redirect("user_wall.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                friendRequestSent = false;
                friendRequestButton = false;
                hasSentFriendRequest = false;
                hasReceivedFriendRequest = false;
                user = uc.getFriendById(anotherUser);
                isFriend = uc.isFriend(anotherUser);
                String profilePrivacy = user.getProfilePrivacy();
                if (isFriend) {
                    switch (profilePrivacy) {
                        case "ONLY_ME":
                            privateUser = true;
                            showPosts(false);
                            break;
                        default:
                            privateUser = false;
                            showPosts(true);
                    }
                } else if (uc.isFriendOfFriends(anotherUser)) {
                    getFriendRequestStatus(anotherUser);
                    switch (profilePrivacy) {
                        case "ONLY_ME":
                            privateUser = true;
                            showPosts(false);
                            break;
                        case "FRIENDS":
                            privateUser = false;
                            showPosts(false);
                            break;
                        default:
                            privateUser = false;
                            showPosts(true);
                    }
                } else {
                    getFriendRequestStatus(anotherUser);
                    switch (profilePrivacy) {
                        case "PUBLIC":
                            privateUser = false;
                            showPosts(true);
                            break;
                        default:
                            privateUser = true;
                            showPosts(false);
                    }
                }
            }
        }
    }

    private void showPosts(boolean show) {
        postDtos = pc.getPostsByUser(user.getEmail());
        leftRightRender = show;
        if (postDtos.size() > 0) {
            noPosts = !show;
        } else {
            leftRightRender = false;
            noPosts = show;
        }
    }

    public void sendFriendRequest() {
        friendRequestButton = false;
        friendRequestSent = true;
        uc.addFriendRequest(user);
    }

    public void getFriendRequestStatus(Long id) {
        hasSentFriendRequest = uc.hasSentFriendRequest(id);
        hasReceivedFriendRequest = uc.hasReceivedFriendRequest(id);
        boolean canContact = false;
        switch (uc.getFriendById(id).getContactUserPrivacy()) {
            case "PUBLIC":
                canContact = true;
                break;
            case "FRIENDS_OF_FRIENDS":
                canContact = true;
                break;
            default:
                canContact = false;
        }
        if (hasSentFriendRequest || hasReceivedFriendRequest || !canContact) {
            friendRequestButton = false;
        } else {
            friendRequestButton = true;
        }
    }

    public void addFriend(Long id) {
        hasSentFriendRequest = false;
        uc.addFriend(uc.getFriendById(id));
    }

    public ProfileDto getUser() {
        return user;
    }

    public void setUser(ProfileDto user) {
        this.user = user;
    }

    public List<PostDto> getPostDtos() {
        return postDtos;
    }

    public void setPostDtos(List<PostDto> postDtos) {
        this.postDtos = postDtos;
    }

    public boolean isNoPosts() {
        return noPosts;
    }

    public void setNoPosts(boolean noPosts) {
        this.noPosts = noPosts;
    }

    public boolean isLeftRightRender() {
        return leftRightRender;
    }

    public void setLeftRightRender(boolean leftRightRender) {
        this.leftRightRender = leftRightRender;
    }

    public boolean isPrivateUser() {
        return privateUser;
    }

    public void setPrivateUser(boolean privateUser) {
        this.privateUser = privateUser;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isFriendRequestSent() {
        return friendRequestSent;
    }

    public void setFriendRequestSent(boolean friendRequestSent) {
        this.friendRequestSent = friendRequestSent;
    }

    public boolean isFriendRequestButton() {
        return friendRequestButton;
    }

    public void setFriendRequestButton(boolean friendRequestButton) {
        this.friendRequestButton = friendRequestButton;
    }

    public boolean isHasSentFriendRequest() {
        return hasSentFriendRequest;
    }

    public void setHasSentFriendRequest(boolean hasSentFriendRequest) {
        this.hasSentFriendRequest = hasSentFriendRequest;
    }

    public boolean isHasReceivedFriendRequest() {
        return hasReceivedFriendRequest;
    }

    public void setHasReceivedFriendRequest(boolean hasReceivedFriendRequest) {
        this.hasReceivedFriendRequest = hasReceivedFriendRequest;
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
                        redirect("friend_wall.xhtml?faces-redirect=true&includeViewParams=true&id=" + user.getId());

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
