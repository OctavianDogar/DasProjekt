package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.ProfileAssembler;
import edu.msg.jbook.backend.model.*;
import edu.msg.jbook.common.dto.ProfileDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

/**
 * Created by ilyesk on 29.07.2016.
 */
@Stateless
public class ProfileAssemblerBean implements ProfileAssembler {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileAssemblerBean.class);

    @Override
    public UserState dtoToModel(ProfileDto dto) throws CommonException {
        UserState userState = null;
        User user = null;
        UserPrivacy privacy = null;
        try {
            user = new User();
            privacy = new UserPrivacy();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setBirthday(dto.getBirthday());
            user.setGender(dto.getGender());
            privacy.setProfilePrivacy(Privacy.toPrivacy(dto.getProfilePrivacy()));
            privacy.setSearchByNameOrEmail(Privacy.toPrivacy(dto.getSearchByNameOrEmailPrivacy()));
            privacy.setContactUser(Privacy.toPrivacy(dto.getContactUserPrivacy()));
            user.setUserPrivacy(privacy);
            userState = new UserState();
            userState.setUser(user);
            userState.setEmail(dto.getEmail());
            userState.setPassword(dto.getNewPassword());
            userState.setPhoto(dto.getPhoto());
            userState.setId(dto.getId());
            userState.getUser().setGender(dto.getGender());
            userState.getUser().setUserType(UserType.valueOf(dto.getType()));
            return userState;
        } catch (IllegalArgumentException e) {
            LOG.error("Cannot convert dto to model.", e);
            throw new CommonException("Cannot convert dto to model.", e);
        }
    }

    @Override
    public ProfileDto modelToDto(UserState model) throws CommonException {
        ProfileDto profileDto = null;
        try {
            profileDto = new ProfileDto();
            profileDto.setEmail(model.getEmail());
            profileDto.setId(model.getId());
            profileDto.setNewPassword(null);
            profileDto.setOldPassword(null);
            profileDto.setGender(model.getUser().isGender());
            profileDto.setBirthday(model.getUser().getBirthday());
            profileDto.setFirstName(model.getUser().getFirstName());
            profileDto.setLastName(model.getUser().getLastName());
            profileDto.setProfilePrivacy(model.getUser().getUserPrivacy().getProfilePrivacy().toString());
            profileDto.setSearchByNameOrEmailPrivacy(model.getUser().getUserPrivacy().getSearchByNameOrEmail().toString());
            profileDto.setContactUserPrivacy(model.getUser().getUserPrivacy().getContactUser().toString());
            profileDto.setPhoto(model.getPhoto());
            profileDto.setAccountStatus(model.getAccountStatus());
            profileDto.setType(model.getUser().getUserType().toString());
            return profileDto;
        } catch (IllegalArgumentException e) {
            LOG.error("Cannot convert model to dto.", e);
            throw new CommonException("Cannot convert model to dto.", e);
        }
    }
}
