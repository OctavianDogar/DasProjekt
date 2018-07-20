package edu.msg.jbook.backend.service;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Privacy;
import edu.msg.jbook.backend.model.UserRelationship;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.common.dto.LoginAndRegisterDto;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.common.dto.SearchDto;

import java.util.List;

/**
 * Created by iacobd on 26.07.2016.
 */

public interface UserService {

    ProfileDto getUserByEmail(String email) throws ServiceException;

    ProfileDto getUserByEmailIncludingDisabledAccounts(String email) throws ServiceException;

    List<ProfileDto> getUserFriendsByEmail(String email) throws ServiceException;

    List<ProfileDto> getUserRequestsByEmail(String email) throws ServiceException;

    UserState insertUser(LoginAndRegisterDto userDto) throws ServiceException;

    void addFriend(ProfileDto user, ProfileDto newFriend) throws ServiceException;

    void addFriendRequest(ProfileDto user, ProfileDto friendRequest) throws ServiceException;

    void removeFriend(ProfileDto user, ProfileDto oldFriend) throws ServiceException;

    void removeRequest(ProfileDto user, ProfileDto friendRequest) throws ServiceException;

    void updateUser(ProfileDto profileDto) throws ServiceException;

    boolean checkLogIn(String email, String pass) throws ServiceException;

    List<SearchDto> searchUsers(String content, String userMail) throws ServiceException;

    boolean checkActivationCode(String uuid, String activationCode) throws ServiceException;

    boolean checkExists(String email) throws ServiceException;

    boolean isFriend(UserState source, UserState target) throws ServiceException;

    boolean hasSentFriendRequest(Long source, Long target) throws ServiceException;

    boolean isFriendofFriends(Long source, Long target) throws ServiceException;

    boolean isFriend(Long source, Long target) throws ServiceException;

    ProfileDto getUserById(Long id) throws ServiceException;

    void updateUserPhoto(String email, String imagePath) throws ServiceException;

    List<ProfileDto> getAllUsers() throws ServiceException;

    void updateAccountStatus(ProfileDto profileDto) throws ServiceException;

}
