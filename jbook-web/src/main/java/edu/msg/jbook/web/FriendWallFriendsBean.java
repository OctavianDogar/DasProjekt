package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ilyesk on 03.08.2016.
 */
@ManagedBean
@RequestScoped
public class FriendWallFriendsBean {
    @EJB
    private UserController uc;
    private ProfileDto user = new ProfileDto();
    private Boolean friendsRender = false;
    private boolean noFriends = false;
    private List<ProfileDto> userFriends = new LinkedList<ProfileDto>();

    @PostConstruct
    public void getFriends() {

        String query = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (query != null && !query.equals("")) {
            user = uc.getFriendById(Long.parseLong(query));
            userFriends = uc.getUserFriends(user.getEmail());
            if(userFriends.size() > 0){
                friendsRender = true;
                noFriends = false;
            }else{
                noFriends = true;
                friendsRender = false;
            }
        }
    }

    public ProfileDto getUser() {
        return user;
    }

    public void setUser(ProfileDto user) {
        this.user = user;
    }

    public Boolean getFriendsRender() {
        return friendsRender;
    }

    public void setFriendsRender(Boolean friendsRender) {
        this.friendsRender = friendsRender;
    }

    public List<ProfileDto> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(List<ProfileDto> userFriends) {
        this.userFriends = userFriends;
    }

    public boolean isNoFriends() {
        return noFriends;
    }

    public void setNoFriends(boolean noFriends) {
        this.noFriends = noFriends;
    }
}
