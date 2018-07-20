package edu.msg.jbook.web;


import edu.msg.jbook.common.dto.*;
import edu.msg.jbook.web.controllers.EventController;
import edu.msg.jbook.web.controllers.PostController;
import edu.msg.jbook.web.controllers.UserController;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by dogaro on 02/08/2016.
 */
@ManagedBean
@ViewScoped
public class SingularEventBean implements Serializable {

    @EJB
    private EventController ec;
    @EJB
    private UserController uc;
    @EJB
    private PostController pc;
    private List<PostDto> eventPosts;
    private ProfileDto admin;
    private EventDto event;
    private boolean isCurrentUserRelatedToEvent = true;
    private List<ParticipantStatusDto> attending;
    private List<ParticipantStatusDto> declined;
    private boolean someGoing = false;
    private boolean someDeclined = false;
    private boolean currentUserIsGoing;
    private boolean currentUserIsAdmin;

    public boolean isCurrentUserIsGoing() {
        currentUserIsGoing = checkIfCurrentUserIsGoing();
        return currentUserIsGoing;
    }

    public void setCurrentUserIsGoing(boolean currentUserIsGoing) {
        this.currentUserIsGoing = currentUserIsGoing;
    }

    public boolean isCurrentUserIsAdmin() {
        return currentUserIsAdmin;
    }

    public void setCurrentUserIsAdmin(boolean currentUserIsAdmin) {
        this.currentUserIsAdmin = currentUserIsAdmin;
    }

    @PostConstruct
    private void seeEvent() {
        String query = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("eventId");
        if (!(query == null && query.equals(""))) {
            Long id = Long.parseLong(query);
            event = ec.getEventById(id);
            attending = ec.getGoingParticipantsAndSatuses(id);
            declined = ec.getNotGoingParticipantsAndSatuses(id);
            if (attending.size() > 0) someGoing = true;
            if (declined.size() > 0) someDeclined = true;
            eventPosts = ec.getEventPosts(id);
            admin = event.getAdmin();
            if (admin.getEmail().equals(uc.getCurrentUser().getEmail())) {
                currentUserIsAdmin = true;
            }
            currentUserIsGoing = isCurrentUserIsGoing();
        }
    }

    public boolean checkIfCurrentUserIsGoing() {
        String currentUserEmail = uc.getCurrentUser().getEmail();
        for (ParticipantStatusDto par : attending) {
            if (par.getProfileDto().getEmail().equals(currentUserEmail)) {
                return true;
            }
        }
        return false;
    }

    public List<ParticipantStatusDto> getAttending() {
        return attending;
    }

    public List<ParticipantStatusDto> getDeclined() {
        return declined;
    }

    public boolean isSomeGoing() {
        return someGoing;
    }

    public boolean isSomeDeclined() {
        return someDeclined;
    }

    public UserController getUc() {
        return uc;
    }

    public void setUc(UserController uc) {
        this.uc = uc;
    }

    public List<PostDto> getEventPosts() {
        return eventPosts;
    }

    public void setEventPosts(List<PostDto> eventPosts) {
        this.eventPosts = eventPosts;
    }

    public ProfileDto getAdmin() {
        return admin;
    }

    public void setAdmin(ProfileDto admin) {
        this.admin = admin;
    }

    public EventDto getEvent() {
        return event;
    }

    public void setEvent(EventDto event) {
        this.event = event;
    }

    public void addLikeToPost(Long postId) {
        pc.addLikeToPost(postId);
        PostDto post = pc.getPostById(postId);
        for (PostDto news : eventPosts) {
            if (news.getId() == postId) {
                news.setLikes(post.getLikes());
            }
        }
    }

    public void addComment(PostDto post) {
        pc.addCommentToPost(post.getId(), post.getTeporaryComment());
        post.setTeporaryComment("");
        PostDto updatedPost = pc.getPostById(post.getId());
        for (PostDto news : eventPosts) {
            if (news.getId() == post.getId()) {
                news.setComments(updatedPost.getComments());
            }
        }
    }

    public void onUserChoosen(SelectEvent event) {
        SearchDto dto = (SearchDto) event.getObject();
        try {
            if (dto == null) {
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect("eventLayout.xhtml?faces-redirect=true&includeViewParams=true&eventId=" + this.event.getId());
            } else {
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect("friend_wall.xhtml?faces-redirect=true&includeViewParams=true&id=" + dto.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void goToEvent() {
        ec.changeMyStatusToEvent(event.getId(), true);
        refresh();
    }

    public void cancelGoingToEvent() {
        ec.changeMyStatusToEvent(event.getId(), false);
        refresh();
    }

    public void refresh() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().
                    redirect("eventLayout.xhtml?faces-redirect=true&includeViewParams=true&eventId=" + this.event.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
}










