package edu.msg.jbook.common.dto;

import java.time.LocalDateTime;

/**
 * Created by dogaro on 02/08/2016.
 */
public class NewsDto {

    private EventDto eventDto;
    private PostDto postDto;
    private boolean postOrEvent;
    private LocalDateTime creationTime;

    public NewsDto() {
    }

    public boolean isPostOrEvent() {
        return postOrEvent;
    }

    public void setPostOrEvent(boolean postOrEvent) {
        this.postOrEvent = postOrEvent;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public EventDto getEventDto() {
        return eventDto;
    }

    public void setEventDto(EventDto eventDto) {
        this.eventDto = eventDto;
    }

    public PostDto getPostDto() {
        return postDto;
    }

    public void setPostDto(PostDto postDto) {
        this.postDto = postDto;
    }
}
