package edu.msg.jbook.web;

import edu.msg.jbook.web.controllers.UserController;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Created by iacobd on 31.07.2016.
 */
@ManagedBean
@RequestScoped
public class HeaderBean {

    @EJB
    private UserController uc;

    public String doGoToLogin(){
        return "login.xhtml";
    }
    public String doGoToRegister(){
        return "register.xhtml";
    }
}
