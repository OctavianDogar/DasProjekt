package edu.msg.jbook.backend.service;

import edu.msg.jbook.common.dto.NewsDto;

import java.util.List;

/**
 * Created by iacobd on 09.08.2016.
 */
public interface NewsService {
    List<NewsDto> getNewsForUser(String mail, int offset, int cap);
}
