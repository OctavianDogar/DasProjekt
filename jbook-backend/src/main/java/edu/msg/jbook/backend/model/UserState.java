package edu.msg.jbook.backend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//commit D
@Entity
@Table(name = "userState")
public class UserState extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;

    private boolean verified;
    private String verifyCode;
    private List<UserState> friends;
    private List<UserState> requests;
    private User user;
    private String photo;

    private boolean accountStatus;

    @Column(name = "verified", nullable = true)
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @ManyToMany
    @JoinTable(name = "friends",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "friendId")})
    public List<UserState> getFriends() {
        return friends;
    }

    public void setFriends(List<UserState> friends) {
        this.friends = friends;
    }

    @ManyToMany
    @JoinTable(name = "requests",
            joinColumns = {@JoinColumn(name = "requestedId")},
            inverseJoinColumns = {@JoinColumn(name = "requesterId")})
    public List<UserState> getRequests() {
        return requests;
    }

    public void setRequests(List<UserState> requests) {
        this.requests = requests;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToOne
    @JoinColumn(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name="photo", nullable = true)
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UserState{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", verified=" + verified +
                ", requests=" + requests +'\n'+
                ", user=" + user +
                '}';
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Column(name="accountStatus", nullable = true, columnDefinition = "boolean default false")
    public boolean getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }
}
