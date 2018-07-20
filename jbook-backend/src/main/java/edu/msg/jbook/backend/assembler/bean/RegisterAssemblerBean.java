package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.RegisterAssembler;
import edu.msg.jbook.backend.model.User;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.model.UserType;
import edu.msg.jbook.common.dto.LoginAndRegisterDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

/**
 * Created by dogaro on 28/07/2016.
 */
@Stateless
public class RegisterAssemblerBean implements RegisterAssembler {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterAssemblerBean.class);

    @Override
    public UserState dtoToModel(LoginAndRegisterDto dto) throws CommonException {
        UserState userState = null;
        User user = null;
        try {
            user = new User();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            if (dto.getType() == null) {
                user.setUserType(UserType.user);
            } else {
                user.setUserType(UserType.valueOf("admin"));
            }
            userState = new UserState();
            userState.setUser(user);
            userState.setEmail(dto.getEmail());
            userState.setPassword(dto.getPassword());
            userState.getUser().setBirthday(dto.getBirthDay());
            return userState;
        } catch (IllegalArgumentException e) {
            LOG.error("Cannot convert dto to model.", e);
            throw new CommonException("Cannot convert dto to model.", e);
        }
    }

    @Override
    public LoginAndRegisterDto modelToDto(UserState model) throws CommonException {
        LoginAndRegisterDto loginAndRegisterDto = null;
        try {
            loginAndRegisterDto = new LoginAndRegisterDto();
            loginAndRegisterDto.setLastName(model.getUser().getLastName());
            loginAndRegisterDto.setFirstName(model.getUser().getFirstName());
            loginAndRegisterDto.setEmail(model.getEmail());
            loginAndRegisterDto.setPassword(model.getPassword());
            loginAndRegisterDto.setBirthDay(model.getUser().getBirthday());
            return loginAndRegisterDto;
        } catch (IllegalArgumentException e) {
            LOG.error("Cannot convert model to dto.", e);
            throw new CommonException("Cannot convert model to dto.", e);
        }
    }
}
