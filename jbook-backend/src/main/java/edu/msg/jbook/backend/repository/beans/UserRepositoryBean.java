package edu.msg.jbook.backend.repository.beans;

import edu.msg.jbook.backend.model.User;
import edu.msg.jbook.backend.repository.UserRepository;

import javax.ejb.Stateless;

/**
 * Created by ilyesk on 28.07.2016.
 */
@Stateless(name="UserRepository",mappedName="ejb/UserRepository")
public class UserRepositoryBean extends BaseRepositoryBean<User,Long> implements UserRepository{

    public UserRepositoryBean() {
        super(User.class);
    }


}
