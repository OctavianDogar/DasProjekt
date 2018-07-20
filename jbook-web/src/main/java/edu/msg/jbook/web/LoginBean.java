package edu.msg.jbook.web;


import edu.msg.jbook.common.dto.LoginAndRegisterDto;
import edu.msg.jbook.common.exceptions.CommonException;
import edu.msg.jbook.web.controllers.UserController;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Created by iacobd on 28.07.2016.
 */
@ManagedBean
@RequestScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private LoginAndRegisterDto user;
    @EJB
    private UserController uc;

    public String processLogin() {
        String resp = uc.checkLogin(user.getEmail(), user.getPassword());
        return resp;
    }

    public String processLogout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        session.invalidate();
        return "login";
    }

    public void setUser(LoginAndRegisterDto user) {
        this.user = user;
    }

    public LoginAndRegisterDto getUser() {
        if (user == null) {
            user = new LoginAndRegisterDto();
        }
        return user;
    }
}