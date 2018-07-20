package edu.msg.jbook.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Created by iacobd on 28.07.2016.
 */
@FacesValidator("passwordValidator")
public class PasswordValidator  implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value)
            throws ValidatorException {
        String stringValue = value.toString();
        if (stringValue.length() < 6) {
            FacesMessage message = new FacesMessage("Password must be at least 6 characters!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
