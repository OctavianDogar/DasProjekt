package edu.msg.jbook.common.dto;


import java.util.Calendar;
import java.util.Date;

/**
 * Created by ilyesk on 29.07.2016.
 */
public class ProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthday;
    private boolean gender;
    private String email;
    private String oldPassword;
    private String newPassword;
    private String profilePrivacy;
    private String searchByNameOrEmailPrivacy;
    private String contactUserPrivacy;
    private String photo;
    private String type;

    private boolean accountStatus;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((id == null) ? 0 : id.hashCode());
        result = prime * result + Integer.parseInt(id.toString(),10);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(obj==null)
            return false;
        ProfileDto other = (ProfileDto) obj;
        if(id == null){
            if(other.getId()!=null)
                return false;
        }else{
            if(!id.equals(other.getId())){
                return false;
            }
        }
        return true;
    }

    public ProfileDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthday);
        int month = cal.get(Calendar.MONTH);

        return Integer.toString(month);
    }

    public String getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthday);
        int year = cal.get(Calendar.YEAR);

        return Integer.toString(year);
    }

    public String getDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthday);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return Integer.toString(day);
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePrivacy() {
        return profilePrivacy;
    }

    public void setProfilePrivacy(String profilePrivacy) {
        this.profilePrivacy = profilePrivacy;
    }

    public String getSearchByNameOrEmailPrivacy() {
        return searchByNameOrEmailPrivacy;
    }

    public void setSearchByNameOrEmailPrivacy(String searchByNameOrEmailPrivacy) {
        this.searchByNameOrEmailPrivacy = searchByNameOrEmailPrivacy;
    }

    public String getContactUserPrivacy() {
        return contactUserPrivacy;
    }

    public void setContactUserPrivacy(String contactUserPrivacy) {
        this.contactUserPrivacy = contactUserPrivacy;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }
}
