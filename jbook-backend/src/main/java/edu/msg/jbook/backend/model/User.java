package edu.msg.jbook.backend.model;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dogaro on 27/07/2016.
 */
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private UserType userType;
    private UserPrivacy privacy;
    private Date birthday;
    private boolean gender;


    @Column(name = "firstName", nullable = false, length = 40)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastName", nullable = false, length = 40)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "birthday", nullable = true)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "gender", nullable = true)
    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="userPrivacy")
    public UserPrivacy getUserPrivacy() {
        return privacy;
    }

    public void setUserPrivacy(UserPrivacy privacy) {
        this.privacy = privacy;
    }

    @Column(name = "userType", nullable = true)
    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userType=" + userType +
                ", privacy=" + privacy +
                ", birthday=" + birthday +
                ", gender=" + gender +
                '}';
    }
}
