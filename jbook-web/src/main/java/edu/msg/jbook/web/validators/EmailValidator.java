package edu.msg.jbook.web.validators;

import edu.msg.jbook.web.controllers.UserController;

import javax.faces.bean.ManagedBean;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 * Created by ilyesk on 01.08.2016.
 */
@RequestScoped
@ManagedBean(name = "emailValidator")
@DependsOn("UserController")

public class EmailValidator implements Validator {
    @EJB
    UserController uc;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String stringValue = o.toString();
        HttpSession session = (HttpSession) FacesContext
                .getCurrentInstance().getExternalContext()
                .getSession(false);
        if(!stringValue.equals(session.getAttribute("email")))
        if (uc.checkExists(stringValue)) {
            FacesMessage message = new FacesMessage("E-mail already exists in database.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
