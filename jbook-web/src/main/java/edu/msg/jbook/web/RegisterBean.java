package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.LoginAndRegisterDto;
import edu.msg.jbook.web.controllers.UserController;
import org.joda.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by marcp on 28.07.2016.
 */
@ManagedBean
@RequestScoped
public class RegisterBean implements Serializable {

    private LoginAndRegisterDto user = new LoginAndRegisterDto();
    private String repeatPassword;
    @EJB
    private UserController uc;

    @PostConstruct
    public void init(){
        user.setGender(true);
    }
    public void processRegister() {
        String result = uc.processRegister(user);
        if (result.equals("registerSuccess"))
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User \""+user.getFirstName() + user
                .getLastName()+"\" " +"was registred successfully"));
    }

    public LoginAndRegisterDto getUser() {
        return user;
    }

    public void setUser(LoginAndRegisterDto user) {
        this.user = user;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value){
        String stringValue = value.toString();
        if (uc.checkExists(stringValue)) {
            FacesMessage message = new FacesMessage("E-mail already exists in database.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }


}
