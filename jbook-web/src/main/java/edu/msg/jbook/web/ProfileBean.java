package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ilyesk on 29.07.2016.
 */
@ManagedBean
@RequestScoped
public class ProfileBean implements Serializable {

    private ProfileDto user = new ProfileDto();
    private String retypedPassword;
    private Date date = new Date();
    @EJB
    private UserController uc;

    @PostConstruct
    public void viewProfile() {
        user = uc.getCurrentUser();
    }

    public void updateProfile() {
        uc.updateProfile(user);
    }

    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String stringValue = value.toString();
        if (!stringValue.equals(user.getEmail()))
            if (uc.checkExists(stringValue)) {
                FacesMessage message = new FacesMessage("E-mail already exists in database.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
    }

    public void validatePassword(FacesContext facesContext, UIComponent uiComponent, Object value) {
        HttpSession session = null;
        session = (HttpSession) FacesContext
                .getCurrentInstance().getExternalContext()
                .getSession(false);
        String email = (String) session.getAttribute("email");
        String stringValue = value.toString();
        if (stringValue.length() > 0)
            if (uc.checkLogin(email, stringValue).equals("loginFailed")) {
                FacesMessage message = new FacesMessage("Incorrect password.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
    }

    public void validateNewPassword(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String stringValue = value.toString();

        if(stringValue.length()>0)
        if (stringValue.length() < 6) {
            FacesMessage message = new FacesMessage("Password must be at least 6 characters!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

    public ProfileDto getUser() {
        return user;
    }

    public void setUser(ProfileDto user) {
        this.user = user;
    }

    public String getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(String retypedPassword) {
        this.retypedPassword = retypedPassword;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void disableAccount(){
        uc.disableAccount();
    }
}
