package edu.msg.jbook.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ilyesk on 27.07.2016.
 */
@Entity
@Table(name="userPrivacy")
public class UserPrivacy extends BaseEntity {
    private Privacy profilePrivacy;
    private Privacy searchByNameOrEmail; /** Optional **/
    private Privacy contactUser;

    @Column(name="profilePrivacy",nullable=false)
    public Privacy getProfilePrivacy() {
        return profilePrivacy;
    }

    public void setProfilePrivacy(Privacy profilePrivacy) {
        this.profilePrivacy = profilePrivacy;
    }

    @Column(name="searchByNameOrEmail")
    public Privacy getSearchByNameOrEmail() {
        return searchByNameOrEmail;
    }


    public void setSearchByNameOrEmail(Privacy searchByName) {
        this.searchByNameOrEmail = searchByName;
    }

    @Column(name="contactUser",nullable=false)
    public Privacy getContactUser() {
        return contactUser;
    }

    public void setContactUser(Privacy contactUser) {
        this.contactUser = contactUser;
    }
}
