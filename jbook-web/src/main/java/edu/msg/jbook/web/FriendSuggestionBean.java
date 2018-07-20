package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.web.controllers.UserController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ilyesk on 04.08.2016.
 */
@ManagedBean
@ViewScoped
public class FriendSuggestionBean {

    @EJB
    private UserController uc;
    private Set<ProfileDto> suggestions = new HashSet<ProfileDto>();
    private boolean render;

    @PostConstruct
    public void init(){
        ProfileDto profile = uc.getCurrentUser();
       suggestions=uc.getUserSuggestionFriends(profile.getEmail());
        if(suggestions.size()>0) {
            render = true;
        }else{
            render = false;
        }
    }

    public void addFriend(ProfileDto friend){
        uc.addFriendRequest(friend);
        suggestions = uc.getUserSuggestionFriends(uc.getCurrentUser().getEmail());
        if(suggestions.size()>0) {
            render = true;
        }else{
            render = false;
        }
    }

    public Set<ProfileDto> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(Set<ProfileDto> suggestions) {
        this.suggestions = suggestions;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}
