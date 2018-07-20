package edu.msg.jbook.web.validators;

import org.joda.time.LocalDate;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;

/**
 * Created by ilyesk on 01.08.2016.
 */
@FacesValidator("calendarValidator")
public class CalendarValidator implements Validator {
    private LocalDate currentDate;
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if(o!=null) {
            currentDate = new LocalDate();
            LocalDate date = (LocalDate) LocalDate.fromDateFields((Date) o);
            if (currentDate.getYear() < date.getYear() || (currentDate.getYear() == date.getYear() && currentDate.getMonthOfYear() < date.getMonthOfYear()) || (currentDate.getYear() == date.getYear() && currentDate.getMonthOfYear() == date.getMonthOfYear() && currentDate.getDayOfMonth() < currentDate.getDayOfMonth())) {
                FacesMessage message = new FacesMessage("Choose a date until: " + currentDate.getYear() + "/" + currentDate.getMonthOfYear() + "/" + currentDate.getDayOfMonth());
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        }else{
            FacesMessage message = new FacesMessage("Fill the calendar field.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
