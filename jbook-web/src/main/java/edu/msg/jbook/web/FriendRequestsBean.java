package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by marcp on 02.08.2016.
 */
@ManagedBean
@RequestScoped
public class FriendRequestsBean implements Serializable {

    @EJB
    private UserController uc;
    private List<ProfileDto> userRequests = new LinkedList<ProfileDto>();
    private boolean requestsRender = false;
    private boolean noRequests = false;
    private int chunckSize;

    public List<ProfileDto> getUserRequests() {
        return userRequests;
    }

    public void setUserRequests(List<ProfileDto> userRequests) {
        this.userRequests = userRequests;
    }

    public boolean isRequestsRender() {
        return requestsRender;
    }

    public void setRequestsRender(boolean requestsRender) {
        this.requestsRender = requestsRender;
    }

    public boolean isNoRequests() {
        return noRequests;
    }

    public void setNoRequests(boolean noRequests) {
        this.noRequests = noRequests;
    }

    public int getChunckSize() {
        return chunckSize;
    }

    public void setChunckSize(int chunckSize) {
        this.chunckSize = chunckSize;
    }

    @PostConstruct
    public void getRequests() {
        userRequests = uc.getUserRequests();
        if (userRequests.size() > 0) {
            requestsRender = true;
            noRequests = false;
            chunckSize = userRequests.size();
        } else {
            requestsRender = false;
            noRequests = true;
            chunckSize = 0;
        }
    }

    public void addFriend(ProfileDto friend) {
        userRequests.remove(friend);
        uc.addFriend(friend);
        chunckSize = userRequests.size();
        if (userRequests.size() == 0) {
            requestsRender = false;
            noRequests = true;
            chunckSize = 0;
        }
    }

    public void ignoreFriendRequest(ProfileDto request) {
        userRequests.remove(request);
        uc.removeFriendRequest(request);
        chunckSize = userRequests.size();
        if (userRequests.size() == 0) {
            requestsRender = false;
            noRequests = true;
            chunckSize = 0;
        }
    }
}
