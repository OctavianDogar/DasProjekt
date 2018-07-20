package edu.msg.jbook.backend.assembler;

import edu.msg.jbook.backend.model.Notification;
import edu.msg.jbook.common.dto.EventDto;
import edu.msg.jbook.common.dto.NewsDto;
import edu.msg.jbook.common.dto.NotificationDto;
import edu.msg.jbook.common.dto.PostDto;

/**
 * Created by ilyesk on 11.08.2016.
 */
public interface NewsAssembler {
    NewsDto postToNews(PostDto dto);
    NewsDto eventToNews(EventDto dto);
}
