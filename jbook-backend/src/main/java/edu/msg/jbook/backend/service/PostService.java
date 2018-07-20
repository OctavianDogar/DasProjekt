package edu.msg.jbook.backend.service;

import edu.msg.jbook.backend.exception.ServiceException;
import edu.msg.jbook.backend.model.Post;
import edu.msg.jbook.common.dto.CommentDto;
import edu.msg.jbook.common.dto.LikeDto;
import edu.msg.jbook.common.dto.PostDto;

import java.util.List;

/**
 * Created by cioncag on 29.07.2016.
 */
public interface PostService {
    List<PostDto> getPostsByUser(String email) throws ServiceException;

    List<PostDto> getAllPosts()throws ServiceException;

    Post insertPost(PostDto dto) throws ServiceException;

    void addLikeToPost(LikeDto dto)throws ServiceException;

    void addCommentToPost(CommentDto dto)throws ServiceException;

    PostDto getPostById(Long id)throws ServiceException;

}
