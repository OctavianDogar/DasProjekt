package edu.msg.jbook.backend.repository.beans;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Privacy;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.backend.repository.UserStateRepository;
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
 * Created by cioncag on 26.07.2016.
 */

@Stateless(name = "UserStateRepository", mappedName = "ejb/UserStateRepository")
public class UserStateRepositoryBean extends BaseRepositoryBean<UserState, Long> implements UserStateRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserStateRepositoryBean.class);
    @PersistenceContext(unitName = "jbook")
    private EntityManager entityManager;

    public UserStateRepositoryBean() {
        super(UserState.class);
    }

    @Override
    public UserState getUserByEmail(String email) throws RepositoryException {
        UserState userState = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.email =?1 and u.accountStatus=?2");
            query.setParameter(1, email);
            query.setParameter(2, false);
            userState = (UserState) query.getSingleResult();
            return filterFriends(userState);

        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by email.", e);
            throw new RepositoryException("Cannot get user by email", e);
        }
    }
    @Override
    public UserState getUserByEmailIncludingDisabledAccounts(String email) throws RepositoryException {
        UserState userState = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.email =?1");
            query.setParameter(1, email);
            userState = (UserState) query.getSingleResult();
            return filterFriends(userState);

        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by email.", e);
            throw new RepositoryException("Cannot get user by email", e);
        }
    }

    @Override
    public UserState getByIdIncludingDisabledAccounts(Long id) throws RepositoryException {
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.id =?1");
            query.setParameter(1, id);
            return filterFriends((UserState) query.getSingleResult());
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by id.", e);
            throw new RepositoryException("Cannot get user by id.", e);
        }
    }

    @Override
    public List<UserState> getUserByName(String firstName, String lastName) throws RepositoryException {
        List<UserState> userState = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.user.firstName LIKE :firstName AND" +
                    " u.user.lastName LIKE :lastName and u.accountStatus=:deactivated");
            query.setParameter("firstName", "%" + firstName + "%");
            query.setParameter("lastName", "%" + lastName + "%");
            query.setParameter("deactivated", false);
            userState = query.getResultList();
            for (UserState us: userState) {
                filterFriends(us);
            }
            return userState;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by name.", e);
            throw new RepositoryException("Cannot get user by name.", e);
        }
    }

    @Override
    public List<UserState> getUsersByEmail(String email) throws RepositoryException {
        List<UserState> userState = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.email LIKE :email and u.accountStatus=:deactivated");
            query.setParameter("email", "%" + email + "%");
            query.setParameter("deactivated", false);
            userState = query.getResultList();
            for (UserState us: userState) {
                filterFriends(us);
            }
            return userState;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by name.", e);
            throw new RepositoryException("Cannot get user by name.", e);
        }
    }

    @Override
    public List<UserState> getUsersBySingleName(String name) throws RepositoryException {
        List<UserState> userState = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.user.firstName LIKE :name " +
                    "OR u.user.lastName LIKE :name and u.accountStatus=:deactivated");
            query.setParameter("name", "%" + name + "%");
            query.setParameter("deactivated", false);
            userState = query.getResultList();
            for (UserState us: userState) {
                filterFriends(us);
            }
            return userState;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by name.", e);
            throw new RepositoryException("Cannot get user by name.", e);
        }
    }

    @Override
    public Privacy getUserPrivacy(UserState userState) throws RepositoryException {
        Privacy privacy = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.user.id =?1 and u.accountStatus=?2");
            query.setParameter(1, userState.getUser().getId());
            query.setParameter(2, false);
            privacy = (Privacy) query.getSingleResult();
            return privacy;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get privacy settings for user.", e);
            throw new RepositoryException("Cannot get privacy settings for user.", e);
        }
    }

    public UserState checkActivationCode(String uuid, String activationCode) throws RepositoryException {
        UserState userState = null;
        UserState userStateUpdated = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.uuid =?1 and u.accountStatus=?2");
            query.setParameter(1, uuid);
            query.setParameter(2, false);
            userState = (UserState) query.getSingleResult();
            if (userState != null) {
                if (userState.getVerifyCode().equals(activationCode)) {
                    userState.setVerified(true);
                    userStateUpdated = super.merge(userState);
                }
            }
            return filterFriends(userStateUpdated);
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by name.", e);
            throw new RepositoryException("Cannot get user by name.", e);
        }
    }

    @Override
    public List<UserState> getAll() throws RepositoryException {
        List<UserState> userState = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.accountStatus=?1");;
            query.setParameter(1, false);
            userState =  query.getResultList();
            for (UserState us: userState) {
                filterFriends(us);
            }
            return userState;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by name.", e);
            throw new RepositoryException("Cannot get user by name.", e);
        }
    }
    @Override
    public List<UserState> getAllIncludingDisabledAccounts() throws RepositoryException {
        List<UserState> userState = null;
        try {
            Query query = entityManager.createQuery("Select u from UserState u");;
            userState =  query.getResultList();
            for (UserState us: userState) {
                filterFriends(us);
            }
            return userState;
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get all users.", e);
            throw new RepositoryException("Cannot get all users.", e);
        }
    }

    @Override
    public UserState getById(Long id) throws RepositoryException {
        try {
            Query query = entityManager.createQuery("Select u from UserState u where u.id =?1 and u.accountStatus=?2");
            query.setParameter(1, id);
            query.setParameter(2, false);
            return filterFriends((UserState) query.getSingleResult());
        } catch (IllegalArgumentException | PersistenceException e) {
            LOG.error("Cannot get user by name.", e);
            throw new RepositoryException("Cannot get user by name.", e);
        }
    }
    private UserState filterFriends(UserState userState){
        try {
            if (userState != null) {
                List<UserState> friends = userState.getFriends();
                userState.setFriends(friends.stream()
                        .filter(friend -> friend.getAccountStatus() == false)
                        .collect(Collectors.toList()));
            }
            return userState;
        }catch(NullPointerException e){
            LOG.error("Cannot filter friends.",e);
            throw new RepositoryException("Cannot filter friends.",e);
        }
    }
}
