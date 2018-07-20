package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.UserEventAssembler;
import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.UserEvent;
import edu.msg.jbook.backend.repository.EventRepository;
import edu.msg.jbook.backend.repository.UserEventRepository;
import edu.msg.jbook.backend.repository.UserStateRepository;
import edu.msg.jbook.common.dto.UserEventDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.Null;

/**
 * Created by dogaro on 01/08/2016.
 */
@Stateless
public class UserEventAssemblerBean implements UserEventAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterAssemblerBean.class);
    @EJB
    private UserEventAssembler userEventAssembler;
    @EJB
    private UserEventRepository userEventRepository;
    @EJB
    private EventRepository eventRepository;
    @EJB
    private UserStateRepository userStateRepository;

    @Override
    public UserEvent dtoToModel(UserEventDto dto) throws CommonException {
        UserEvent userEvent = null;
        try {
            userEvent = new UserEvent();
            userEvent.setId(dto.getId());
            userEvent.setEvent(eventRepository.getById(dto.getEventId()));
            userEvent.setUserState(userStateRepository.getById(dto.getUserStateId()));
            userEvent.setStatus(dto.isStatus());
            return userEvent;
        } catch (NullPointerException | RepositoryException e) {
            LOG.error("Cannot convert dto to model.", e);
            throw new CommonException("Cannot convert dto to model.", e);
        }
    }

    @Override
    public UserEventDto modelToDto(UserEvent model) throws CommonException {
        UserEventDto userEventDto = null;
        try {
            userEventDto = new UserEventDto();
            userEventDto.setId(model.getId());
            userEventDto.setStatus(model.getStatus());
            userEventDto.setEventId(model.getEvent().getId());
            userEventDto.setUserStateId(model.getUserState().getId());
            return userEventDto;
        } catch (NullPointerException e) {
            LOG.error("Cannot convert model to dto.", e);
            throw new CommonException("Cannot convert model to dto.", e);
        }
    }
}
