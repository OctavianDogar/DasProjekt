package edu.msg.jbook.common.dto;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dogaro on 29/07/2016.
 */
public class EventDto {

    private String text;
    private Long id;
    private ProfileDto admin;
    private Date dateTime;
    private String title;
    private String dateString;
    private List<UserEventDto> participants;
    private LocalDateTime creationTime;

    public EventDto() {
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserEventDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserEventDto> participants) {
        this.participants = participants;
    }

    public ProfileDto getAdmin() {
        return admin;
    }

    public void setAdmin(ProfileDto admin) {
        this.admin = admin;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
        dateString = getDay() + "/" + getMonth() + "/" + getYear() + " at " + getHour() + ":" + getMinute();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((id == null) ? 0 : id.hashCode());
        result = prime * result + Integer.parseInt(id.toString(), 10);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        EventDto other = (EventDto) obj;
        if (id == null) {
            if (other.getId() != null)
                return false;
        } else {
            if (!id.equals(other.getId())) {
                return false;
            }
        }
        return true;
    }

    private String getMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        int month = cal.get(Calendar.MONTH);
        return Integer.toString(month);
    }

    private String getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        int year = cal.get(Calendar.YEAR);
        return Integer.toString(year);
    }

    private String getDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return Integer.toString(day);
    }

    private String getHour() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        int day = cal.get(Calendar.HOUR_OF_DAY);
        return Integer.toString(day);
    }

    private String getMinute() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        int day = cal.get(Calendar.MINUTE);
        return Integer.toString(day);
    }
}
