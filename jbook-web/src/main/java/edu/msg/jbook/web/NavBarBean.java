package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.controllers.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iacobd on 29.07.2016.
 */
@ManagedBean(name="navbarBean")
@ApplicationScoped
public class NavBarBean implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(NavBarBean.class);
    private List<SearchDto> selectedUsers = new ArrayList<SearchDto>();
    private SearchDto user = new SearchDto();
    private boolean render;
    @EJB
    private UserController uc;
    private String value;


    public String doSearch() {
        return "searchSuccess";
    }

//    public String doSearch(String value) {
//        return "search.xhtml?faces-redirect=true&includeViewParams=true&query=" + value;
//    }

    public SearchDto getById(Long id){
        for(SearchDto s: selectedUsers)
            if(s.getId()==id) {
                return s;
            }
        return null;
    }

    public List<SearchDto> completeSearch(String query) {
        render=true;
        selectedUsers = uc.searchByName(query);
        value= new String(query);
        return selectedUsers;
    }

    public Long getId(String name){
        return uc.searchByName(name).get(0).getId();
    }

    public void doLogout(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        session.invalidate();
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        try {
            ec.redirect("login.xhtml");
        } catch (IOException e) {
            LOG.error("Error logging out.",e);
        }
    }

    public List<SearchDto> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<SearchDto> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public SearchDto getUser() {
        return user;
    }

    public void setUser(SearchDto user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}

