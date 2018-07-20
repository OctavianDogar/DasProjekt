package edu.msg.jbook.web;

import edu.msg.jbook.web.controllers.UserController;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Created by cioncag on 01.08.2016.
 */
@ManagedBean
@ApplicationScoped
public class ConfirmationBean {

    private static final long serialVersionUID = 1L;
    private String activationCode = "";
    @EJB
    private UserController uc;
    private String urlRequest;

    public ConfirmationBean() {
    }

    public String processConfirmation() {
        String uuidValue = urlRequest.substring(urlRequest.indexOf("uuid=") + 5, urlRequest.length());
        return uc.checkActivationCode(uuidValue, activationCode);
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public UserController getUc() {
        return uc;
    }

    public void setUc(UserController uc) {
        this.uc = uc;
    }

    public String getUrlRequest() {
        return urlRequest;
    }

    public void setUrlRequest(String urlRequest) {
        this.urlRequest = urlRequest;
    }
}
