package edu.msg.jbook.common.dto;

/**
 * Created by dogaro on 03/08/2016.
 */
public class ParticipantStatusDto {

    private ProfileDto profileDto;
    private boolean status;

    public ParticipantStatusDto(ProfileDto profileDto, boolean status) {
        this.profileDto = profileDto;
        this.status = status;
    }

    public ParticipantStatusDto() {
    }

    public ProfileDto getProfileDto() {
        return profileDto;
    }

    public void setProfileDto(ProfileDto profileDto) {
        this.profileDto = profileDto;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


}
