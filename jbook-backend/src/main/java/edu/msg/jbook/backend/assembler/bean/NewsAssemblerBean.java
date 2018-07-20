package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.NewsAssembler;
import edu.msg.jbook.common.dto.EventDto;
import edu.msg.jbook.common.dto.NewsDto;
import edu.msg.jbook.common.dto.PostDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.OneToOne;

/**
 * Created by dogaro on 02/08/2016.
 */
@Stateless
public class NewsAssemblerBean implements NewsAssembler{

    private static final Logger LOG = LoggerFactory.getLogger(NewsAssemblerBean.class);

    @Override
    public NewsDto postToNews(PostDto dto) throws CommonException{
        NewsDto newsDto = null;
        try {
            newsDto = new NewsDto();
            newsDto.setPostDto(dto);
            newsDto.setCreationTime(dto.getCreationTime());
            newsDto.setPostOrEvent(true);
            return newsDto;
        }catch(NullPointerException e){
            LOG.error("Cannot convert post model to dto.",e);
            throw new CommonException("Cannot convert model to dto.",e);
        }
    }

    @Override
    public NewsDto eventToNews(EventDto dto) throws CommonException{
        NewsDto newsDto = null;
        try {
            newsDto = new NewsDto();
            newsDto.setEventDto(dto);

            newsDto.setCreationTime(dto.getCreationTime());
            newsDto.setPostOrEvent(false);
            return newsDto;
        }catch(NullPointerException e){
            LOG.error("Cannot convert event model to dto.",e);
            throw new CommonException("Cannot convert event model to dto.",e);
        }
    }
}
