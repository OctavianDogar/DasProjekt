package edu.msg.jbook.web;

import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.common.dto.*;
import edu.msg.jbook.web.controllers.AdminUserController;
import edu.msg.jbook.web.controllers.EventController;
import edu.msg.jbook.web.controllers.UserController;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by cioncag on 04.08.2016.
 */
@ManagedBean
@SessionScoped
public class AdminBean {
    @EJB
    UserController uc;
    @EJB
    AdminUserController auc;
    @EJB
    EventController ec;
    List<ProfileDto> profileDtos;
    List<String> accountDisplayStatus;
    ProfileDto currentProfile = new ProfileDto();
    private boolean showAllUsers = true;
    private boolean showEditProfile = false;
    List<EventDto> eventDtos;
    EventDto currentEvent = new EventDto();
    private boolean renderEventsTable;
    private boolean noParticipantsFlag = false;
    private int participantsChunk;
    private List<ParticipantStatusDto> participantStatuses;
    private LoginAndRegisterDto registredUser = new LoginAndRegisterDto();
    private boolean renderEditEvent;
    Map<Long, Integer> mappedParticipants;
    List<ProfileDto> currentEventsParticipants;

    public AdminBean() {
    }

    @PostConstruct
    public void init() {
        profileDtos = auc.getAllUsers();
        accountDisplayStatus = new ArrayList<>();
        for(ProfileDto profileDto : profileDtos) {
            profileDto.setOldPassword("");
            if(profileDto.getAccountStatus()) {
                accountDisplayStatus.add("Activate");
            } else {
                accountDisplayStatus.add("Deactivate");
            }
        }
        eventDtos = ec.getAllEvents();
        renderEventsTable = true;
        renderEditEvent = false;
        mappedParticipants = ec.getAllParticipantsNumbers();
        participantsChunk = 10;
        registredUser.setGender(true);
        registredUser.setType("false");
    }

    public boolean isRenderEventsTable() {
        return renderEventsTable;
    }

    public void setRenderEventsTable(boolean renderEventsTable) {
        this.renderEventsTable = renderEventsTable;
    }

    public boolean isRenderEditEvent() {
        return renderEditEvent;
    }

    public void setRenderEditEvent(boolean renderEditEvent) {
        this.renderEditEvent = renderEditEvent;
    }

    public Map<Long, Integer> getMappedParticipants() {
        return mappedParticipants;
    }

    public void setMappedParticipants(Map<Long, Integer> mappedParticipants) {
        this.mappedParticipants = mappedParticipants;
    }

    public void displayEditEvent(Long id){
        List<EventDto> events = getEventDtos();
        for (EventDto ed : events){
            if (ed.getId() == id){
                currentEvent = ed;
                break;
            }
        }
        participantStatuses = ec.getParticipantsAndSatusesByEventId(id);
        if(participantStatuses.size()>0){
            noParticipantsFlag = true;
        }
        renderEventsTable = false;
        renderEditEvent = true;
    }

    public void updateEvent(){
        String result = ec.updateEvent(currentEvent);
        if(result.equals("updateSuccess")) {
            clearFacesMessage();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Event updated successfully"));
        }
        ec.updateEventParticipants(participantStatuses, currentEvent.getId());
        currentEvent = null;
        noParticipantsFlag = false;
        renderEventsTable = true;
        renderEditEvent = false;
    }

    public void removeEvent(Long id){
        ec.removeEvent(id);
        clearFacesMessage();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Event removed successfully"));
    }

    public void clearFacesMessage(){
        Iterator<FacesMessage> msgIterator = FacesContext.getCurrentInstance().getMessages();
        while (msgIterator.hasNext())
        {
            FacesMessage facesMessage = msgIterator.next();
            msgIterator.remove();
        }
    }


    public void validateEventName(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String stringValue = value.toString();
        if (!stringValue.equals(currentEvent.getTitle()))
            if (uc.checkExists(stringValue)) {
            clearFacesMessage();
                FacesMessage message =
                        new FacesMessage("Event with the given name already exists in database.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
    }

    public void processRegister() {
        String result = uc.processRegister(registredUser);
        if (result.equals("registerSuccess"))
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User \""+registredUser.getFirstName() + registredUser
                    .getLastName()+"\" " +"was registred successfully"));
    }

    public void validateRegistredEmail(FacesContext facesContext, UIComponent uiComponent, Object value){
        String stringValue = value.toString();
        if (uc.checkExists(stringValue)) {
            FacesMessage message = new FacesMessage("E-mail already exists in database.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

    public List<EventDto> getEventDtos() {
        return ec.getAllEvents();
    }

    public void setEventDtos(List<EventDto> eventDtos) {
        this.eventDtos = eventDtos;
    }

    public EventDto getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(EventDto currentEvent) {
        this.currentEvent = currentEvent;
    }

    public List<ProfileDto> getProfileDtos() {
        init();
        return profileDtos;
    }

    public void setProfileDtos(List<ProfileDto> profileDtos) {
        this.profileDtos = profileDtos;
    }

    public boolean isShowAllUsers() {
        return showAllUsers;
    }

    public void setShowAllUsers(boolean showAllUsers) {
        this.showAllUsers = showAllUsers;
    }

    public boolean getShowEditProfile() {
        return showEditProfile;
    }

    public void setShowEditProfile(boolean showEditProfile) {
        this.showEditProfile = showEditProfile;
    }

    public void displayEditProfile(Long id) {
        for(ProfileDto profileDto : profileDtos) {
            if(profileDto.getId() == id) {
                currentProfile = profileDto;
                break;
            }
        }

        showAllUsers = false;
        showEditProfile = true;
    }

    public void deactivateAccount(Long id) {
        int profileIndex = 0;
        for(ProfileDto profileDto : profileDtos) {
            if(profileDto.getId() == id) {
                if(profileDto.getAccountStatus()) {
                    accountDisplayStatus.set(profileIndex, "Deactivate");
                    profileDto.setAccountStatus(false);
                } else {
                    accountDisplayStatus.set(profileIndex, "Activate");
                    profileDto.setAccountStatus(true);
                }
                currentProfile = profileDto;
                break;
            }
            profileIndex++;
        }
        uc.updateAccountStatus(currentProfile);
    }

    public ProfileDto getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(ProfileDto currentProfile) {
        this.currentProfile = currentProfile;
    }

    public String getAccountStatus(Long id) {
        int accoutStatusIndex = 0;
        for(ProfileDto profileDto : profileDtos) {
            if(profileDto.getId() == id) {
                return accountDisplayStatus.get(accoutStatusIndex);
            }
            accoutStatusIndex++;
        }
        return "";
    }

    public void updateProfile() {
        String resultMessage = uc.updateProfile(currentProfile);
        int profileIndex = 0;
        if(resultMessage.equals("updateSuccess")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Account updated successfully"));
        }
        for(ProfileDto profileDto : profileDtos) {
            if(currentProfile.getId() == profileDto.getId()) {
                break;
            }
            profileIndex++;
        }
        profileDtos.set(profileIndex, currentProfile);
        this.showEditProfile = false;
        this.showAllUsers = true;
    }

    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String stringValue = value.toString();
        if (!stringValue.equals(currentProfile.getEmail()))
            if (uc.checkExists(stringValue)) {
                FacesMessage message = new FacesMessage("E-mail already exists in database.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
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
            e.printStackTrace();
        }
    }

    public boolean isNoParticipantsFlag() {
        return noParticipantsFlag;
    }

    public void setNoParticipantsFlag(boolean noParticipantsFlag) {
        this.noParticipantsFlag = noParticipantsFlag;
    }

    public List<ProfileDto> getCurrentEventsParticipants() {
        return currentEventsParticipants;
    }

    public void setCurrentEventsParticipants(List<ProfileDto> currentEventsParticipants) {
        this.currentEventsParticipants = currentEventsParticipants;
    }

    public int getParticipantsChunk() {
        return participantsChunk;
    }

    public void setParticipantsChunk(int participantsChunk) {
        this.participantsChunk = participantsChunk;
    }

    public List<ParticipantStatusDto> getParticipantStatuses() {
        return participantStatuses;
    }

    public void setParticipantStatuses(List<ParticipantStatusDto> participantStatuses) {
        this.participantStatuses = participantStatuses;
    }

    public LoginAndRegisterDto getRegistredUser() {
        return registredUser;
    }

    public void setRegistredUser(LoginAndRegisterDto registredUser) {
        this.registredUser = registredUser;
    }
}
