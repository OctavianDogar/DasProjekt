package edu.msg.jbook.backend.repository;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.Privacy;
import edu.msg.jbook.backend.model.UserState;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by ilyesk on 26.07.2016.
 */
@Local
public interface UserStateRepository extends BaseRepository<UserState, Long> {

    UserState getUserByEmail(String email) throws RepositoryException;

    List<UserState> getUserByName(String firstName, String lastName) throws RepositoryException;

    Privacy getUserPrivacy(UserState userState) throws RepositoryException;

    List<UserState> getUsersByEmail(String email) throws RepositoryException;

    List<UserState> getUsersBySingleName(String name) throws RepositoryException;

    UserState checkActivationCode(String uuid, String activationCode) throws RepositoryException;

    List<UserState> getAllIncludingDisabledAccounts() throws RepositoryException;

    UserState getUserByEmailIncludingDisabledAccounts(String email) throws RepositoryException;

    UserState getByIdIncludingDisabledAccounts(Long id) throws RepositoryException;

}
