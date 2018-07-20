package edu.msg.jbook.backend.repository;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.Post;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by ilyesk on 27.07.2016.
 */
@Local
public interface PostRepository extends BaseRepository<Post, Long> {

    List<Post> getPostsByParentPost(Post parentPost) throws RepositoryException;

    List<Post> getPostsByEvent(Event event) throws RepositoryException;

    List<Post> getPostsByUser(String email) throws RepositoryException;
}
