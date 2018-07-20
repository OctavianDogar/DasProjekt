package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cioncag on 29.07.2016.
 */
@ManagedBean
@RequestScoped
public class SearchBean implements Serializable {

    @EJB
    private UserController uc;
    private String content;
    private List<SearchDto> resultUsers = new LinkedList<>();
    private boolean render = false;
    private int chunkSize;
    private SearchDto selectedUser;
    private Boolean noResults;

    public void processSearch() {
        resultUsers = uc.searchByName(content);
        chunkSize = resultUsers.size();
        render = true;
    }
    @PostConstruct
    public void init() {
        NavBarBean nb = (NavBarBean)FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("navbarBean");
        String query = nb.getValue();
        if(query!=null && !query.equals("")) {
            resultUsers = uc.searchByName(query);
            chunkSize = resultUsers.size();
            if (chunkSize == 0) {
                noResults = false;
            } else {
                noResults = true;
            }
            render = true;
        }
    }

    public List<SearchDto> getResultUsers() {
        return resultUsers;
    }

    public void setResultUsers(List<SearchDto> resultUsers) {
        this.resultUsers = resultUsers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public UserController getUc() {
        return uc;
    }

    public void setUc(UserController uc) {
        this.uc = uc;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public SearchDto getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(SearchDto selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Boolean getNoResults() {
        return noResults;
    }

    public void setNoResults(Boolean noResults) {
        this.noResults = noResults;
    }
}
