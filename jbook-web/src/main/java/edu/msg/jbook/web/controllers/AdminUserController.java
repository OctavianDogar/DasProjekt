package edu.msg.jbook.web.controllers;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.service.UserService;
import edu.msg.jbook.common.dto.ProfileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;

/**
 * Created by iacobd on 11.08.2016.
 */
@ManagedBean
@Stateless
@DependsOn("UserService")
public class AdminUserController implements Serializable {

    private final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @EJB
    UserService userService;

    public List<ProfileDto> getAllUsers() {
        List<ProfileDto> users = null;
        try {
            users = userService.getAllUsers();
        } catch (ServiceException e) {
            LOG.error("Cannot get all users.", e);
        }
        return users;
    }

}
