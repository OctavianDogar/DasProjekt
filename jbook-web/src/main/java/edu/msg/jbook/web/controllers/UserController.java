package edu.msg.jbook.web.controllers;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.service.UserService;
import edu.msg.jbook.common.dto.LoginAndRegisterDto;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.common.dto.SearchDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by cioncag on 28/07/2016.
 */
@ManagedBean
@Stateless
@DependsOn("UserService")
public class UserController implements Serializable {

    private final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @EJB
    UserService userService;

    public String checkLogin(String email, String password) {
        ProfileDto loggedUser = null;
        HttpSession session = null;
        try {
            if (userService.checkLogIn(email, password)) {
                loggedUser = userService.getUserByEmailIncludingDisabledAccounts(email);
                if (loggedUser.getAccountStatus())
                    return "deactivatedAccount";
                if (loggedUser.getType().equals("admin")) {
                    session = (HttpSession) FacesContext
                            .getCurrentInstance().getExternalContext()
                            .getSession(false);
                    session.setAttribute("email", email);
                    session.setAttribute("isAdmin", "true");
                    return "adminLoginSuccess";
                }
                session = (HttpSession) FacesContext
                        .getCurrentInstance().getExternalContext()
                        .getSession(false);
                session.setAttribute("email", email);
                session.setAttribute("isUser", "true");
                return "loginSuccess";
            }
        } catch (ServiceException e) {
            LOG.error("Login failed.", e);
        } catch (EJBTransactionRolledbackException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid username/password!"));
            LOG.error("Invalid username/password!", e);
        }
        return "loginFailed";
    }

    public String processRegister(LoginAndRegisterDto dto) {
        try {
            userService.insertUser(dto);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("A confirmation email has been sent to your email address!"));
            return "registerSuccess";
        } catch (ServiceException e) {
            LOG.error("Register failed.", e);
        } catch (EJBTransactionRolledbackException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("E-mail already in use!"));
            LOG.error("E-mail already in use.", e);
        }
        return "registerFailed";
    }

    public List<SearchDto> searchByName(String content) {
        List<SearchDto> results = null;
        HttpSession session = null;
        String userMail = null;
        try {
            session = (HttpSession) FacesContext
                    .getCurrentInstance().getExternalContext()
                    .getSession(false);
            userMail = session.getAttribute("email").toString();
            results = userService.searchUsers(content, userMail);
        } catch (ServiceException e) {
            LOG.error("Cannot search by name", e);
        }
        return results;
    }

    public String updateProfile(ProfileDto profileDto) {
        try {
            userService.updateUser(profileDto);
            return "updateSuccess";
        } catch (ServiceException e) {
            LOG.error("Update failed.", e);
            return "updateFailed";
        }
    }

    public ProfileDto getCurrentUser() {
        ProfileDto profileDto = null;
        HttpSession session = null;
        String email = null;
        try {
            session = (HttpSession) FacesContext
                    .getCurrentInstance().getExternalContext()
                    .getSession(false);
            email = (String) session.getAttribute("email");
            profileDto = userService.getUserByEmail(email);
        } catch (ServiceException e) {
            LOG.error("User retrieval failed.", e);
        }
        return profileDto;
    }

    public String checkActivationCode(String uuid, String activationCode) {
        boolean result = false;
        try {
            result = userService.checkActivationCode(uuid, activationCode);
            if (result) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("You have successfully confirmed your account!"));
                return "confirmationSuccess";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Activation Code is incorrect!"));
                LOG.error("Account Activation Failed!");
            }
        } catch (ServiceException e) {
            LOG.error("Cannot check activation code.", e);
        }
        return "confirmationFailed";

    }


    public List<ProfileDto> getUserFriends(String email) {
        List<ProfileDto> users = null;
        try {
            users = userService.getUserFriendsByEmail(email);
        } catch (ServiceException e) {
            LOG.error("Cannot get friends of user.", e);
        }
        return users;
    }

    private HashSet<ProfileDto> toSet(List<ProfileDto> users) {
        HashSet<ProfileDto> user = null;
        try {
            user = new HashSet<ProfileDto>();
            for (Iterator<ProfileDto> it = users.iterator(); it.hasNext(); ) {
                user.add(it.next());
            }
        } catch (ConcurrentModificationException e) {
            LOG.error("Cannot convert list to set.");
        }
        return user;
    }

    public Set<ProfileDto> getUserSuggestionFriends(String email) {
        List<ProfileDto> friends = null;
        List<ProfileDto> suggestions = null;
        Set<ProfileDto> finalSuggestions = null;
        Iterator<ProfileDto> its = null;
        Random rand = null;
        try {
            friends = userService.getUserFriendsByEmail(email);
            suggestions = new ArrayList<ProfileDto>();
            finalSuggestions = new HashSet<>();
            its = friends.iterator();
            rand = new Random();
            while (its.hasNext()) {
                ProfileDto p = its.next();
                HashSet<ProfileDto> temp = toSet(userService.getUserFriendsByEmail(p.getEmail()));
                Iterator<ProfileDto> it = temp.iterator();
                while (it.hasNext()) {
                    ProfileDto tf = it.next();
                    if (!isFriend(tf.getId()) && !tf.getEmail().equals(email) && !isRequestSent(email, tf.getEmail())) {
                        if(!(tf.getContactUserPrivacy().equals("ONLY_ME") || tf.getContactUserPrivacy().equals("FRIENDS"))) {
                            suggestions.add(tf);
                        }
                    }
                }
            }
            if (suggestions.size() > 0)
                while (finalSuggestions.size() < 5 && finalSuggestions.size() < suggestions.size()) {
                    int inte = rand.nextInt(suggestions.size());
                    finalSuggestions.add(suggestions.get(inte));
                }
        } catch (ServiceException e) {
            LOG.error("Cannot get friend suggestions.", e);
        }
        return finalSuggestions;
    }

    private boolean isRequestSent(String senderEmail, String receiverEmail) {
        List<ProfileDto> requests = null;
        try {
            requests = userService.getUserRequestsByEmail(receiverEmail);
            if (requests.contains(userService.getUserByEmail(senderEmail))) {
                return true;
            }
        } catch (ServiceException e) {
            LOG.error("Cannot check whether request is sent.", e);
        }
        return false;
    }

    public List<ProfileDto> getUserRequests() {
        List<ProfileDto> profileDtos = null;
        try {
            profileDtos = userService.getUserRequestsByEmail(getCurrentUser().getEmail());
        } catch (ServiceException e) {
            LOG.error("Cannot get requests for user.");
        }
        return profileDtos;
    }

    public boolean checkExists(String email) {
        try {
            if (userService.checkExists(email)) {
                return true;
            }
        } catch (ServiceException e) {
            LOG.error("Email checking failed.", e);
        }
        return false;
    }

    public void addFriend(ProfileDto friend) {
        try {
            userService.addFriend(getCurrentUser(), friend);
        } catch (ServiceException e) {
            LOG.error("Cannot add friend", e);
        }

    }

    public void addFriendRequest(ProfileDto request) {
        try {
            userService.addFriendRequest(request, getCurrentUser());
        } catch (ServiceException e) {
            LOG.error("Cannot add friend request.", e);
        }
    }

    public void removeFriendRequest(ProfileDto request) {
        try {
            userService.removeRequest(getCurrentUser(), request);
        } catch (ServiceException e) {
            LOG.error("Cannot remove friend request.", e);
        }
    }

    public void removeFriend(ProfileDto friend) {
        try {
            userService.removeFriend(getCurrentUser(), friend);
        } catch (ServiceException e) {
            LOG.error("Cannot remove friend.", e);
        }
    }

    public boolean isFriend(Long target) {
        boolean isFriend = false;
        try {
            isFriend = userService.isFriend(getCurrentUser().getId(), target);
        } catch (ServiceException e) {
            LOG.error("Cannot check whether target is friend.", e);
        }
        return isFriend;
    }

    public boolean isFriendOfFriends(Long target) {
        boolean isFriendOfFriend = false;
        try {
            isFriendOfFriend = userService.isFriendofFriends(getCurrentUser().getId(), target);
        } catch (ServiceException e) {
            LOG.error("Cannot check whether target is a friend of friend.");
        }
        return isFriendOfFriend;
    }

    public boolean hasSentFriendRequest(Long source) {
        boolean hasSentFriendRequest = false;
        try {
            hasSentFriendRequest = userService.hasSentFriendRequest(getCurrentUser().getId(), source);
        } catch (ServiceException e) {
            LOG.error("Cannot check whether source has sent a friend request.", e);
        }
        return hasSentFriendRequest;
    }

    public boolean hasReceivedFriendRequest(Long target) {
        boolean hasReceivedFriendRequest = false;
        try {
            hasReceivedFriendRequest = userService.hasSentFriendRequest(target, getCurrentUser().getId());
        } catch (ServiceException e) {
            LOG.error("Cannot check whether target has received friend request.", e);
        }
        return hasReceivedFriendRequest;
    }

    public boolean isUser(Long target) {
        boolean isUser = false;
        try {
            isUser = getCurrentUser().getId().equals(target);
        } catch (ServiceException e) {
            LOG.error("Cannot check target is user.", e);
        }
        return isUser;
    }

    public ProfileDto getFriendById(Long id) {
        ProfileDto profileDto = null;
        try {
            profileDto = userService.getUserById(id);

        } catch (ServiceException e) {
            LOG.error("Cannot get user by id", e);
        }
        return profileDto;
    }

    public void updateUserPhoto(String email, String imagePath) {
        try {
            userService.updateUserPhoto(email, imagePath);
        } catch (ServiceException e) {
            LOG.error("Cannot update user photo.", e);
        }
    }


    public void updateAccountStatus(ProfileDto profileDto) {
        try {
            userService.updateAccountStatus(profileDto);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("The account was updated successfully!"));
        } catch (ServiceException e) {
            LOG.error("Cannot update account.", e);
        }
    }

    public void disableAccount() {
        ProfileDto user = null;
        HttpSession session = null;
        try {
            user = getCurrentUser();
            user.setAccountStatus(true);
            updateAccountStatus(user);
            session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } catch (IOException e) {
            LOG.error("Error logging out.", e);
        } catch (ServiceException e) {
            LOG.error("Cannot disable account.", e);
        }

    }
}
