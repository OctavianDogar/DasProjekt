package edu.msg.jbook.web.controllers;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.service.NewsService;
import edu.msg.jbook.common.dto.NewsDto;
import edu.msg.jbook.common.dto.ProfileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by dogaro on 02/08/2016.
 */
@ManagedBean
@Stateless
@DependsOn({"EventService", "PostService", "UserService"})
public class NewsController {

    private final Logger LOG = LoggerFactory.getLogger(NewsController.class);
    @EJB
    private UserController userController;
    @EJB
    private NewsService newsService;

    public List<NewsDto> getNews(int offset, int cap) {
        List<NewsDto> newsDtos = null;
        ProfileDto currentUser = null;
        try {
            currentUser = userController.getCurrentUser();
            newsDtos = newsService.getNewsForUser(currentUser.getEmail(), offset, cap);
        } catch (ServiceException e) {
            LOG.error("Failed to get news for user.", e);
        }
        return newsDtos;
    }
}
