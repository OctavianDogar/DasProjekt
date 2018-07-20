package edu.msg.jbook.backend.repository.beans;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.backend.model.Post;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ilyesk on 27.07.2016.
 */
@Stateless(name = "PostRepository", mappedName = "ejb/PostRepository")
public class PostRepositoryBean extends BaseRepositoryBean<Post, Long> implements PostRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PostRepositoryBean.class);
    @PersistenceContext(unitName = "jbook")
    private EntityManager em;

    public PostRepositoryBean() {
        super(Post.class);
    }

    @Override
    public List<Post> getPostsByParentPost(Post parentPost) throws RepositoryException {
        List<Post> posts = null;
        try {
            Query query = em.createQuery("SELECT a FROM Post a WHERE a.parentOfComment=?1 and a.owner.accountStatus =?2");
            query.setParameter(1, parentPost);
            query.setParameter(2, false);
            posts = query.getResultList();
            for (Post p:posts) {
                filterDisabledUserLikes(p);
            }
            return posts;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get comments of posts", e);
            throw new RepositoryException("Cannot get comments of posts", e);
        }
    }

    @Override
    public List<Post> getPostsByEvent(Event event) throws RepositoryException {
        List<Post> posts = null;
        try {
            Query query = em.createQuery("SELECT a FROM Post a WHERE a.event=?1 and a.owner.accountStatus =?2");
            query.setParameter(1, event);
            query.setParameter(2, false);
            posts = query.getResultList();
            for (Post p:posts) {
                filterDisabledUserLikes(p);
            }
            return posts;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get posts of the specified event.", e);
            throw new RepositoryException("Cannot get posts of the specified event.", e);
        }
    }

    @Override
    public List<Post> getPostsByUser(String email) throws RepositoryException {
        List<Post> posts = null;
        try {
            Query query = em.createQuery("SELECT a FROM Post a where a.owner.email=?1 and a.owner.accountStatus =?2");
            query.setParameter(1, email);
            query.setParameter(2, false);
            posts = query.getResultList();
            for (Post p:posts) {
                filterDisabledUserLikes(p);
            }
            return posts;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get posts by user.", e);
            throw new RepositoryException("Cannot get posts by user.", e);
        }
    }

    @Override
    public List<Post> getAll() throws RepositoryException {
        List<Post> posts = null;
        try {
            Query query = em.createQuery("SELECT a FROM Post a where a.owner.accountStatus =?1");
            query.setParameter(1, false);
            posts = query.getResultList();
            for (Post p:posts) {
                filterDisabledUserLikes(p);
            }
            return posts;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Get all failed.", e);
            throw new RepositoryException("Get all failed.", e);
        }
    }

    @Override
    public Post getById(Long id) throws RepositoryException {
        Post post = null;
        try {
            Query query = em.createQuery("SELECT a FROM Post a where a.id =?1 and a.owner.accountStatus =?2");
            query.setParameter(1, id);
            query.setParameter(2, false);
            post = (Post) query.getSingleResult();
            return filterDisabledUserLikes(post);
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Get all failed.", e);
            throw new RepositoryException("Get all failed.", e);
        }
    }

    public Post filterDisabledUserLikes(Post post) {
        if (post != null) {
            List<UserState> likes = post.getUserLikes();
            post.setUserLikes(likes.stream()
                    .filter(like -> like.getAccountStatus() == false)
                    .collect(Collectors.toList()));
        }
        return post;
    }
}
