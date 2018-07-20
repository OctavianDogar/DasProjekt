package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

/**
 * Created by iacobd on 02.08.2016.
 */
@ManagedBean
@RequestScoped
public class HomeBean implements Serializable{

    @EJB
    private UserController uc;
    private ProfileDto currentUser;


    public ProfileDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(ProfileDto currentUser) {
        this.currentUser = currentUser;
    }

    @PostConstruct
    public void init(){
        currentUser = uc.getCurrentUser();
    }
}
