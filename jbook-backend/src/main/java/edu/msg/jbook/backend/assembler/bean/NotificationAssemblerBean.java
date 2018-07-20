package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.NotificationAssembler;
import edu.msg.jbook.backend.assembler.SearchAssembler;
import edu.msg.jbook.backend.model.Notification;
import edu.msg.jbook.common.dto.NotificationDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.security.InvalidParameterException;


/**
 * Created by iacobd on 08.08.2016.
 */
@Stateless
public class NotificationAssemblerBean implements NotificationAssembler {
    private static final Logger LOG = LoggerFactory.getLogger(SearchAssemblerBean.class);

    @EJB
    SearchAssembler sa;

    @Override
    public Notification dtoToModel(NotificationDto dto) throws CommonException {
        try {
            return null;
        }catch(InvalidParameterException e){
            LOG.error("Cannot convert dto to model.",e);
            throw new CommonException("Cannot convert dto to model.",e);
        }
    }

    @Override
    public NotificationDto modelToDto(Notification model) throws CommonException {
        NotificationDto notificationDto = null;
        try {
            notificationDto = new NotificationDto();
            notificationDto.setSeen(model.getSeen());
            notificationDto.setId(model.getId());
            notificationDto.setAuthor(sa.modelToDto(model.getAuthor()));
            notificationDto.setMessage(model.getDescription());
            notificationDto.setType(model.getType());
            notificationDto.setDate(model.getTime());
            if(model.getEvent()!=null){
                notificationDto.setEventName(model.getEvent().getTitle());
            }
            switch (model.getType()) {
                case COMMENT_OR_LIKE:
                    notificationDto.setTypeId(model.getPost().getId());
                    break;
                case POST:
                case EVENT_IN_A_DAY:
                case EVENT_MODIFIED:
                    notificationDto.setTypeId(model.getEvent().getId());
                    break;
                case FRIEND_ACCEPT:
                    notificationDto.setTypeId(model.getAuthor().getId());
                    break;
                case BIRTHDAY:
                    notificationDto.setTypeId(model.getAuthor().getId());
                    break;
            }
            return notificationDto;
        } catch (IllegalArgumentException |CommonException e) {
            LOG.error("Cannot convert dto to model.", e);
            throw new CommonException("Cannot convert dto to model.", e);
        }
    }
}
