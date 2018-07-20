package edu.msg.jbook.web.controllers;


import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.service.PostService;
import edu.msg.jbook.backend.service.UserService;
import edu.msg.jbook.common.dto.CommentDto;
import edu.msg.jbook.common.dto.LikeDto;
import edu.msg.jbook.common.dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;

/**
 * Created by cioncag on 29.07.2016.
 */
@ManagedBean
@Stateless
@DependsOn("PostService")
public class PostController implements Serializable {

    private final Logger LOG = LoggerFactory.getLogger(PostController.class);
    @EJB
    PostService postService;
    @EJB
    UserService userService;
    @EJB
    UserController userController;

    public List<PostDto> getPostsByUser(String email) {
        List<PostDto> postDtos = null;
        try {
            postDtos = postService.getPostsByUser(email);
        } catch (ServiceException e) {
            LOG.error("Cannot get posts by user.", e);
        }
        return postDtos;
    }

    public void createPost(PostDto postDto) {
        try {
            postDto.setEmail(userController.getCurrentUser().getEmail());
            postService.insertPost(postDto);
        } catch (ServiceException e) {
            LOG.error("Cannot create post.", e);
        }
    }

    public void createPostInEvent(PostDto createdPost, Long eventId) {
        try {
            createdPost.setEmail(userController.getCurrentUser().getEmail());
            createdPost.setEventId(eventId);
            postService.insertPost(createdPost);
        } catch (ServiceException e) {
            LOG.error("Cannot create post in event.", e);
        }
    }

    public void addLikeToPost(Long postId) {
        String email = null;
        LikeDto likeDto = null;
        try {
            email = userController.getCurrentUser().getEmail();
            likeDto = new LikeDto();
            likeDto.setEmail(email);
            likeDto.setPostId(postId);
            postService.addLikeToPost(likeDto);
        } catch (ServiceException e) {
            LOG.error("Cannot add like to a post", e);
        }
    }

    public void addCommentToPost(Long postId, String text) {
        String email = null;
        CommentDto comm = null;
        try {
            email = userController.getCurrentUser().getEmail();
            comm = new CommentDto();
            comm.setCommentText(text);
            comm.setPosterMail(email);
            comm.setParentId(postId);
            postService.addCommentToPost(comm);
        } catch (ServiceException e) {
            LOG.error("Cannot add comment to a post.", e);
        }
    }

    public PostDto getPostById(Long id) {
        PostDto post = null;
        try{
            post = postService.getPostById(id);
        }catch(ServiceException e){
            LOG.error("Cannot get post by id.",e);
        }
        return post;
    }
}
