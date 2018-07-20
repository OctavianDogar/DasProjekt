package edu.msg.jbook.backend.service.beans;

import edu.msg.jbook.backend.assembler.CommentAssembler;
import edu.msg.jbook.backend.assembler.PostAssembler;
import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.Post;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.EventRepository;
import edu.msg.jbook.backend.repository.PostRepository;
import edu.msg.jbook.backend.repository.UserStateRepository;
import edu.msg.jbook.backend.service.NotificationService;
import edu.msg.jbook.backend.service.PostService;
import edu.msg.jbook.common.dto.CommentDto;
import edu.msg.jbook.common.dto.LikeDto;
import edu.msg.jbook.common.dto.PostDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cioncag on 29.07.2016.
 */

@Stateless
@DependsOn({"PostRepository"})
public class PostServiceBean implements PostService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceBean.class);
    @EJB
    NotificationService notificationService;
    @EJB
    private PostRepository postRepositoryBean;
    @EJB
    private UserStateRepository userStateRepositoryBean;
    @EJB
    private EventRepository eventRepository;
    @EJB
    private PostAssembler postAssembler;
    @EJB
    private CommentAssembler commentAssembler;

    @Override
    public List<PostDto> getPostsByUser(String email) throws ServiceException {
        List<Post> posts = null;
        List<Post> postWithoutParentOrEvent = null;
        List<PostDto> postDtos = null;
        try {
            posts = postRepositoryBean.getPostsByUser(email);
            postWithoutParentOrEvent = posts.stream()
                    .filter(postDto ->
                            (postDto.getParentOfComment() == null && postDto.getEvent() == null))
                    .collect(Collectors.toList());
            postDtos = new LinkedList<>();
            for (Post post : postWithoutParentOrEvent) {
                List<Post> commentsForPost = postRepositoryBean.getPostsByParentPost(post);
                List<CommentDto> commentDtos = new ArrayList<>();
                for (Post comment : commentsForPost) {
                    commentDtos.add(commentAssembler.modelToDto(comment));
                }
                PostDto postDto = postAssembler.modelToDto(post);
                postDto.setComments(commentDtos);
                postDtos.add(postDto);
            }
            return postDtos;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Get Posts failed.", e);
            throw new ServiceException("Get user by email failed.", e);
        }
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<PostDto> results = null;
        List<Post> allPosts = null;
        List<Post> postWithoutParentOrEvent = null;
        try {
            results = new LinkedList<>();
            allPosts = postRepositoryBean.getAll();
            postWithoutParentOrEvent = allPosts.stream()
                    .filter(postDto ->
                            (postDto.getParentOfComment() == null && postDto.getEvent() == null))
                    .collect(Collectors.toList());
            for (Post post : postWithoutParentOrEvent) {
                PostDto postDto = postAssembler.modelToDto(post);
                List<CommentDto> comms = postRepositoryBean.getPostsByParentPost(post).stream()
                        .map(p -> commentAssembler.modelToDto(p)).collect(Collectors.toList());
                postDto.setComments(comms);
                results.add(postDto);
            }
            return results;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Inserting event failed");
            throw new ServiceException("Inserting event failed");
        }
    }

    @Override
    public Post insertPost(PostDto dto) throws ServiceException {
        Post post = null;
        Event event = null;
        UserState userState = null;
        try {
            userState = userStateRepositoryBean.getUserByEmail(dto.getEmail());
            post = postAssembler.dtoToModel(dto);
            post.setOwner(userState);
            post.setCreationTime(LocalDateTime.now());
            if (dto.getEventId() != null) {
                event = eventRepository.getById(dto.getEventId());
                post.setEvent(event);
                dto.setOwnerId(post.getOwner().getId());
                notificationService.addNotificationForEvent(dto, event.getId());
            }
            return postRepositoryBean.save(post);
        } catch (RepositoryException | CommonException | ServiceException e) {
            LOG.error("Inserting event failed");
            throw new ServiceException("Inserting event failed");
        }
    }


    @Override
    public void addLikeToPost(LikeDto dto) throws ServiceException {
        Post post = null;
        UserState user = null;
        try {
            post = postRepositoryBean.getById(dto.getPostId());
            user = userStateRepositoryBean.getUserByEmail(dto.getEmail());
            post.getUserLikes().add(user);
            postRepositoryBean.merge(post);
            if (!dto.getEmail().equals(post.getOwner().getEmail())) {
                notificationService.addNotificationByLikeDto(dto);
            }
        } catch (RepositoryException | ServiceException e) {
            LOG.error("Unable to add like to post.", e);
            throw new ServiceException("Unable to add like to post.", e);
        }

    }

    @Override
    public void addCommentToPost(CommentDto dto) throws ServiceException {
        Post parentPost = null;
        UserState user = null;
        Post comm = new Post();
        try {
            parentPost = postRepositoryBean.getById(dto.getParentId());
            user = userStateRepositoryBean.getUserByEmail(dto.getPosterMail());
            comm.setText(dto.getCommentText());
            comm.setOwner(user);
            comm.setCreationTime(LocalDateTime.now());
            comm.setParentOfComment(parentPost);
            dto.setId(comm.getId());
            dto.setUserId(comm.getOwner().getId());
            postRepositoryBean.save(comm);
            dto.setId(comm.getId());
            notificationService.addNotificationByCommentDto(dto);
        } catch (RepositoryException | ServiceException e) {
            LOG.error("Unable to add a post comment to post.", e);
            throw new ServiceException("Unable to add a post comment to post.", e);
        }
    }

    @Override
    public PostDto getPostById(Long id) {
        Post parent = null;
        PostDto post = null;
        List<CommentDto> comms = null;
        try {
            parent = postRepositoryBean.getById(id);
            post = postAssembler.modelToDto(parent);
            comms = postRepositoryBean.getPostsByParentPost(parent).stream().map(p -> commentAssembler.modelToDto(p)).
                    collect(Collectors.toList());
            post.setComments(comms);
            return post;
        } catch (RepositoryException | CommonException e) {
            LOG.error("Unable to get post by id.", e);
            throw new ServiceException("Unable to get post by id.", e);
        }
    }

}
