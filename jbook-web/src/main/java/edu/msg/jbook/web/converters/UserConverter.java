package edu.msg.jbook.web.converters;

import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.NavBarBean;
import org.primefaces.context.ApplicationContext;
import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by ilyesk on 03.08.2016.
 */
@FacesConverter("userConverter")
public class UserConverter implements Converter {
    @EJB
    NavBarBean nb;

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        SearchDto searchDto;
        if (isInteger(s))
            if (s != null && s.trim().length() > 0) {
                try {
                    nb = (NavBarBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("navbarBean");
                    searchDto = nb.getById(Long.parseLong(s));
                    nb.setValue(searchDto.getFirstName()+" "+searchDto.getLastName());
                    return nb.getById(Long.parseLong(s));
                } catch (NumberFormatException e) {
                    throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion error", "Not a valid User"));
                }
            }
        searchDto = new SearchDto();
        return searchDto;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("navbarBean", FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("navbarBean"));
            return String.valueOf(((SearchDto) o).getId());
        }
        return null;
    }
}
