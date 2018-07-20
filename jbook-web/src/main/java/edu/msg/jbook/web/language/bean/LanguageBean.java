package edu.msg.jbook.web.language.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Locale;

/**
 * Created by marcp on 10.08.2016.
 */
@ManagedBean
@SessionScoped
public class LanguageBean {

    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        this.locale = new Locale(language);
    }

}
