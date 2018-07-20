package edu.msg.jbook.backend.repository;

import edu.msg.jbook.backend.model.User;

import javax.ejb.Local;

/**
 * Created by ilyesk on 28.07.2016.
 */
@Local
public interface UserRepository extends BaseRepository<User, Long> {
}
