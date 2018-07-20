package edu.msg.jbook.backend.service.beans;

import edu.msg.jbook.backend.assembler.ProfileAssembler;
import edu.msg.jbook.backend.assembler.RegisterAssembler;
import edu.msg.jbook.backend.assembler.SearchAssembler;
import edu.msg.jbook.backend.exception.EncryptionException;
import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Privacy;
import edu.msg.jbook.backend.model.UserPrivacy;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.UserRepository;
import edu.msg.jbook.backend.repository.UserStateRepository;
import edu.msg.jbook.backend.service.NotificationService;
import edu.msg.jbook.backend.service.UserService;
import edu.msg.jbook.backend.util.MailSender;
import edu.msg.jbook.backend.util.PasswordEncryptorSHA1;
import edu.msg.jbook.common.dto.LoginAndRegisterDto;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static edu.msg.jbook.backend.model.Privacy.PUBLIC;

/**
 * Created by iacobd on 26.07.2016.
 */
@Stateless
@DependsOn({"UserStateRepository"})
public class UserServiceBean implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceBean.class);
    @EJB
    NotificationService notificationService;
    @EJB
    private UserStateRepository userStateRepository;
    @EJB
    private UserRepository userRepositoryBean;
    @EJB
    private RegisterAssembler registerAssembler;
    @EJB
    private SearchAssembler searchAssembler;
    @EJB
    private ProfileAssembler profileAssembler;

    @Override
    public ProfileDto getUserByEmail(String email) throws ServiceException {
        UserState userState = null;
        ProfileDto profileDto = null;
        try {
            userState = userStateRepository.getUserByEmail(email);
            if (userState != null) {
                profileDto = profileAssembler.modelToDto(userState);
                return profileDto;
            }
            return null;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get user by email failed.", e);
            throw new ServiceException("Get user by email failed.", e);
        }
    }

    @Override
    public ProfileDto getUserByEmailIncludingDisabledAccounts(String email) throws ServiceException {
        UserState userState = null;
        ProfileDto profileDto = null;
        try {
            userState = userStateRepository.getUserByEmailIncludingDisabledAccounts(email);
            if (userState != null) {
                profileDto = profileAssembler.modelToDto(userState);
                return profileDto;
            }
            return null;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get user by email failed.", e);
            throw new ServiceException("Get user by email failed.", e);
        }
    }

    @Override
    public List<ProfileDto> getUserFriendsByEmail(String email) throws ServiceException {
        UserState userState = null;
        List<ProfileDto> profileDtos = null;
        try {
            userState = userStateRepository.getUserByEmail(email);
            profileDtos = new LinkedList<ProfileDto>();
            for (UserState friend : userState.getFriends()) {
                profileDtos.add(profileAssembler.modelToDto(friend));
            }
            return profileDtos;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get user friends by email failed.", e);
            throw new ServiceException("Get user friends by email failed.", e);
        }
    }

    @Override
    public List<ProfileDto> getUserRequestsByEmail(String email) throws ServiceException {
        UserState userState = null;
        List<ProfileDto> profileDtos;
        try {
            userState = userStateRepository.getUserByEmail(email);
            profileDtos = new LinkedList<ProfileDto>();
            for (UserState request : userState.getRequests()) {
                profileDtos.add(profileAssembler.modelToDto(request));
            }
            return profileDtos;
        } catch (RepositoryException e) {
            LOG.error("Get user requests by email failed.", e);
            throw new ServiceException("Get user requests by email failed.", e);
        }
    }

    public UserState getUserStateByEmail(String email) throws ServiceException {
        try {
            return userStateRepository.getUserByEmail(email);
        } catch (RepositoryException e) {
            LOG.error("Get user by email failed.", e);
            throw new ServiceException("Get user by email failed.", e);
        }
    }

    public List<UserState> getUsersByEmail(String email) throws ServiceException {
        try {
            return userStateRepository.getUsersByEmail(email);
        } catch (RepositoryException e) {
            LOG.error("Get user by email failed.", e);
            throw new ServiceException("Get user by email failed.", e);
        }
    }

    public List<UserState> getUserByName(String firstName, String lastName) throws ServiceException {
        try {
            return userStateRepository.getUserByName(firstName, lastName);

        } catch (RepositoryException e) {
            LOG.error("Get user by name failed.", e);
            throw new ServiceException("Get user by name failed.", e);
        }
    }

    public List<UserState> getUsersBySingleName(String name) throws ServiceException {
        try {
            return userStateRepository.getUsersBySingleName(name);
        } catch (RepositoryException e) {
            LOG.error("Get user by name failed.", e);
            throw new ServiceException("Get user by name failed.", e);
        }
    }

    @Override
    public UserState insertUser(LoginAndRegisterDto userDto) throws ServiceException {
        UserState userStateRet = null;
        UserPrivacy privacy = null;
        String activationCode = null;
        Random random = null;
        try {
            userStateRet = registerAssembler.dtoToModel(userDto);
            userStateRet.setPhoto("butman.jpg");
            privacy = new UserPrivacy();
            privacy.setContactUser(PUBLIC);
            privacy.setSearchByNameOrEmail(PUBLIC);
            privacy.setProfilePrivacy(PUBLIC);
            userStateRet.getUser().setUserPrivacy(privacy);
            userStateRet.setPassword(PasswordEncryptorSHA1.encryptPassword(userStateRet.getPassword()));
            userRepositoryBean.save(userStateRet.getUser());
            activationCode = "";
            random = new Random();
            for (int i = 0; i < 6; i++) {
                activationCode += random.nextInt(9);
            }
            userStateRet.setVerifyCode(activationCode);
            userStateRepository.save(userStateRet);
            MailSender.sendEmail(userStateRet);
            return userStateRet;
        } catch (RepositoryException | EncryptionException e) {
            LOG.error("User insertion failed.", e);
            throw new ServiceException("User insertion failed.", e);
        } catch (EJBTransactionRolledbackException e) {
            LOG.error("Username already in use.", e);
            throw new ServiceException("Username already in use.", e);
        }
    }

    @Override
    public void addFriend(ProfileDto user, ProfileDto newFriend) throws ServiceException {
        UserState userModel = null;
        List<UserState> userFriends = null;
        UserState newFriendModel = null;
        List<UserState> newFriendFriends = null;
        try {
            userModel = getUserStateByEmail(user.getEmail());
            userFriends = userModel.getFriends();
            newFriendModel = getUserStateByEmail(newFriend.getEmail());
            userFriends.add(newFriendModel);
            newFriendFriends = newFriendModel.getFriends();
            newFriendFriends.add(userModel);
            userModel.setFriends(userFriends);
            newFriendModel.setFriends(newFriendFriends);
            userStateRepository.merge(userModel);
            userStateRepository.merge(newFriendModel);
            notificationService.addNotificationForFriendAcceptance(user, newFriend);
            removeRequest(user, newFriend);
        } catch (RepositoryException | ServiceException e) {
            LOG.error("Cannot add friend.", e);
            throw new ServiceException("Cannot add friend.", e);
        }
    }

    @Override
    public void addFriendRequest(ProfileDto user, ProfileDto friendRequest) throws ServiceException {
        UserState userModel = null;
        List<UserState> userModelRequests = null;
        UserState friendRequestModel = null;
        try {
            userModel = getUserStateByEmail(user.getEmail());
            userModelRequests = userModel.getRequests();
            friendRequestModel = getUserStateByEmail(friendRequest.getEmail());
            userModelRequests.add(friendRequestModel);
            userStateRepository.merge(userModel);
            userStateRepository.merge(friendRequestModel);
        } catch (RepositoryException e) {
            LOG.error("Cannot add friend request.", e);
            throw new ServiceException("Cannot add friend request.", e);
        }
    }

    @Override
    public void removeFriend(ProfileDto user, ProfileDto oldFriend) throws ServiceException {
        UserState oldFriendModel = null;
        UserState userModel = null;
        List<UserState> crtFriendFriends = null;
        List<UserState> userFriends = null;
        try {
            userModel = getUserStateByEmail(user.getEmail());
            userFriends = userModel.getFriends();
            oldFriendModel = getUserStateByEmail(oldFriend.getEmail());
            userFriends.remove(oldFriendModel);
            crtFriendFriends = oldFriendModel.getFriends();
            crtFriendFriends.remove(userModel);
            userStateRepository.merge(userModel);
            userStateRepository.merge(oldFriendModel);
        } catch (RepositoryException e) {
            LOG.error("Cannot remove friend.", e);
            throw new ServiceException("Cannot remove friend.", e);
        }
    }

    @Override
    public void removeRequest(ProfileDto user, ProfileDto friendRequest) throws ServiceException {
        UserState friendRequestModel = null;
        UserState userModel = null;
        List<UserState> userModelRequests = null;
        try {
            userModel = getUserStateByEmail(user.getEmail());
            userModelRequests = userModel.getRequests();
            friendRequestModel = getUserStateByEmail(friendRequest.getEmail());
            userModelRequests.remove(friendRequestModel);
            userStateRepository.merge(userModel);
            userStateRepository.merge(friendRequestModel);
        } catch (RepositoryException e) {
            LOG.error("Cannot remove request.", e);
            throw new ServiceException("Cannot remove request.", e);
        }
    }

    @Override
    public void updateUser(ProfileDto profileDto) throws ServiceException {
        UserState userState = null;
        UserState deAssembled = null;
        try {
            userState = userStateRepository.getById(profileDto.getId());
            deAssembled = profileAssembler.dtoToModel(profileDto);
            if (checkLogIn(profileDto.getEmail(), profileDto.getOldPassword())) {
                if (deAssembled.getPassword() != null) {
                    userState.setPassword(PasswordEncryptorSHA1.encryptPassword(deAssembled.getPassword()));
                }
            }
            if (!deAssembled.getUser().getLastName().equals(userState.getUser().getLastName())) {
                userState.getUser().setLastName(deAssembled.getUser().getLastName());
            }
            if (!deAssembled.getUser().getFirstName().equals(userState.getUser().getFirstName())) {
                userState.getUser().setFirstName(deAssembled.getUser().getFirstName());
            }
            if (!deAssembled.getUser().getBirthday().equals(userState.getUser().getBirthday()) && deAssembled.getUser().getBirthday() != null) {
                userState.getUser().setBirthday(deAssembled.getUser().getBirthday());
            }
            if (deAssembled.getUser().isGender() != userState.getUser().isGender()) {
                userState.getUser().setGender(deAssembled.getUser().isGender());
            }
            if (!deAssembled.getEmail().equals(userState.getEmail())) {
                userState.setEmail(deAssembled.getEmail());
            }
            if (!deAssembled.getPhoto().equals(userState.getPhoto())) {
                if (deAssembled.getPhoto() != null)
                    userState.setPhoto(deAssembled.getPhoto());
            }
            if (!deAssembled.getUser().getUserPrivacy().getProfilePrivacy().equals(userState.getUser().getUserPrivacy().getProfilePrivacy())) {
                userState.getUser().getUserPrivacy().setProfilePrivacy(deAssembled.getUser().getUserPrivacy().getProfilePrivacy());
            }
            if (!deAssembled.getUser().getUserPrivacy().getSearchByNameOrEmail().equals(userState.getUser().getUserPrivacy().getSearchByNameOrEmail())) {
                userState.getUser().getUserPrivacy().setSearchByNameOrEmail(deAssembled.getUser().getUserPrivacy().getSearchByNameOrEmail());
            }
            if (!deAssembled.getUser().getUserPrivacy().getContactUser().equals(userState.getUser().getUserPrivacy().getContactUser())) {
                userState.getUser().getUserPrivacy().setContactUser(deAssembled.getUser().getUserPrivacy().getContactUser());
            }
            if (deAssembled.getUser().isGender() != userState.getUser().isGender()) {
                userState.getUser().setGender(deAssembled.getUser().isGender());
            }
            userStateRepository.merge(userState);
        } catch (RepositoryException | EncryptionException e) {
            LOG.error("Update userState failed.", e);
            throw new ServiceException("Update userState failed", e);
        }
    }

    @Override
    public boolean checkLogIn(String email, String pass) throws ServiceException {
        String hashedPass = null;
        UserState dbUserState;
        try {
            hashedPass = PasswordEncryptorSHA1.encryptPassword(pass);
            dbUserState = userStateRepository.getUserByEmailIncludingDisabledAccounts(email);
        } catch (RepositoryException | EncryptionException e) {
            LOG.error("Checking user login failed.", e);
            throw new ServiceException("Checking user login failed.", e);
        }
        if (hashedPass.equals(dbUserState.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public List<SearchDto> searchUsers(String nameOrEmail, String sourceMail) throws ServiceException {
        UserState userSource = null;
        List<SearchDto> results = null;
        List<UserState> filteredUsers = null;
        List<UserState> allUsersMatching = null;
        try {
            results = new LinkedList<SearchDto>();
            filteredUsers = new LinkedList<UserState>();
            allUsersMatching = getAllUsersByNameOrMail(nameOrEmail);
            userSource = getUserStateByEmail(sourceMail);
            for (UserState friend : allUsersMatching) {
                Privacy searchprivacy = friend.getUser().getUserPrivacy().getSearchByNameOrEmail();
                if (friend.getEmail().equals(sourceMail)) {
                    filteredUsers.add(friend);
                } else {
                    switch (searchprivacy) {
                        case PUBLIC: {
                            filteredUsers.add(friend);
                            break;
                        }
                        case FRIENDS_OF_FRIENDS: {
                            if (isFriend(userSource, friend) || isFriendofFriends(userSource, friend)) {
                                filteredUsers.add(friend);
                                break;
                            }
                        }
                        case FRIENDS: {
                            if (isFriend(userSource, friend)) {
                                filteredUsers.add(friend);
                            }
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
            for (UserState u : filteredUsers) {
                results.add(searchAssembler.modelToDto(u));
            }
            return results;
        } catch (ServiceException | CommonException e) {
            LOG.error("Error searching users.", e);
            throw new ServiceException("Error searching users.", e);
        }
    }

    @Override
    public boolean checkExists(String email) throws ServiceException {
        try {
            userStateRepository.getUserByEmailIncludingDisabledAccounts(email);
            return true;
        } catch (RepositoryException e) {
            LOG.error("Error checking whether email exists in database.", e);
            return false;
        }
    }

    @Override
    public ProfileDto getUserById(Long id) throws ServiceException {
        UserState userState = null;
        ProfileDto profileDto = null;
        try {
            userState = userStateRepository.getById(id);
            profileDto = profileAssembler.modelToDto(userState);
            return profileDto;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Cannot get user by id.", e);
            throw new ServiceException("Cannot get user by id.", e);
        }
    }

    private boolean isFriendofFriends(UserState userSource, UserState userTarget) {
        List<UserState> friendsOfSource = userSource.getFriends();
        for (UserState friend : friendsOfSource) {
            if (isFriend(friend, userTarget))
                return true;
        }
        return false;
    }

    @Override
    public boolean isFriend(UserState userSource, UserState userTarget) throws ServiceException {
        List<UserState> friendsOfSource = null;
        try {
            userSource.setFriends(userStateRepository.getById(userSource.getId()).getFriends());
            friendsOfSource = userSource.getFriends();
            for (UserState friend : friendsOfSource) {
                if (friend.getEmail().equals(userTarget.getEmail()))
                    return true;
            }
            return false;
        } catch (RepositoryException e) {
            LOG.error("Cannot check whether target is friend of source.", e);
            throw new ServiceException("Cannot check whether target is friend of source.", e);
        }
    }

    @Override
    public boolean hasSentFriendRequest(Long source, Long target) throws ServiceException {
        try {
            UserState src = userStateRepository.getById(source);
            UserState trg = userStateRepository.getById(target);
            List<UserState> requestsOfSource = src.getRequests();
            for (UserState request : requestsOfSource) {
                if (request.getId().equals(trg.getId())) {
                    return true;
                }
            }
            return false;
        } catch (RepositoryException e) {
            LOG.error("Cannot check whether the friend request was sent.", e);
            throw new ServiceException("Cannot check whether friend request was sent.", e);
        }
    }

    private List<UserState> getAllUsersByNameOrMail(String content) throws ServiceException {
        Pattern pattern;
        Matcher matcher;
        List<UserState> users = null;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String FIRST_OR_LAST = "^[A-Za-z]+$";
        try {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(content);
            if (matcher.matches()) {
                String username = content.substring(0, content.indexOf("@"));
                users = getUsersByEmail(username);
            } else {
                String names[] = new String[2];
                if (content.contains(" ")) {
                    names[0] = content.substring(0, content.indexOf(" "));
                    names[1] = content.substring(content.indexOf(" ") + 1, content.length());
                    if (names[0].equals("")) {
                        if (names[1].contains(" ")) {
                            names[0] = names[1].substring(0, names[1].indexOf(" "));
                            names[1] = names[1].substring(names[1].indexOf(" ") + 1, names[1].length());
                            users = getUserByName(names[0], names[1]);
                        } else {
                            users = getUsersBySingleName(names[1]);
                        }
                    } else {
                        users = getUserByName(names[0], names[1]);
                    }
                } else {
                    pattern = Pattern.compile(FIRST_OR_LAST);
                    matcher = pattern.matcher(content);
                    if (matcher.matches()) {
                        users = getUsersBySingleName(content);
                    }
                }
            }
            return users;
        } catch (RepositoryException | NullPointerException e) {
            LOG.error("Error getting all users by name or e-mail", e);
            throw new ServiceException("Error getting all users by name or e-mail", e);
        }
    }

    @Override
    public boolean checkActivationCode(String uuid, String activationCode) throws ServiceException {
        boolean result = false;
        UserState userState = null;
        try {
            userState = userStateRepository.checkActivationCode(uuid, activationCode);
            if (userState != null) {
                if (userState.getVerifyCode().equals(activationCode)) {
                    result = true;
                }
            } else {
                result = false;
            }
            return result;
        } catch (RepositoryException e) {
            LOG.error("Account Activation Failed!", e);
            throw new ServiceException("Account Activation Failed!", e);
        }
    }

    @Override
    public boolean isFriendofFriends(Long source, Long target) throws ServiceException {
        UserState src = null;
        UserState trg = null;
        try {
            src = userStateRepository.getById(source);
            trg = userStateRepository.getById(target);
            return isFriendofFriends(src, trg);
        }catch(RepositoryException e){
            LOG.error("Cannot check whether source is friend of friend of target.",e);
            throw new ServiceException("Cannot check whether source is friend of friend of target.",e);
        }
    }

    @Override
    public boolean isFriend(Long source, Long target) throws ServiceException{
        UserState src = null;
        UserState trg = null;
        try {
            src = userStateRepository.getById(source);
            trg = userStateRepository.getById(target);
            return isFriend(src, trg);
        }catch(RepositoryException e){
            LOG.error("Cannot check whether source is a friend of target.",e);
            throw new ServiceException("Cannot check whether source is a friend of target.",e);
        }
    }

    @Override
    public void updateUserPhoto(String email, String imagePath) throws ServiceException{
        UserState userState = null;
        try {
            userState = userStateRepository.getUserByEmailIncludingDisabledAccounts(email);
            userState.setPhoto(imagePath);
            userStateRepository.merge(userState);
        }catch(RepositoryException e){
            LOG.error("Cannot update photo.",e);
            throw new ServiceException("Cannot update photo.",e);
        }
    }

    @Override
    public List<ProfileDto> getAllUsers() throws ServiceException{
        try{
        return userStateRepository.getAllIncludingDisabledAccounts().stream()
                .map(userState -> profileAssembler.modelToDto(userState))
                .collect(Collectors.toList());
        }catch (RepositoryException | CommonException e){
            LOG.error("Cannot get all users.",e);
            throw new ServiceException("Cannot get all users.",e);
        }
    }

    @Override
    public void updateAccountStatus(ProfileDto profileDto) throws ServiceException{
        try {
            UserState userState = userStateRepository.getByIdIncludingDisabledAccounts(profileDto.getId());
            userState.setAccountStatus(profileDto.getAccountStatus());
            userStateRepository.merge(userState);
        }catch(RepositoryException e){
            LOG.error("Cannot update account status.",e);
            throw new ServiceException("Cannot update account status.",e);
        }
    }

}
