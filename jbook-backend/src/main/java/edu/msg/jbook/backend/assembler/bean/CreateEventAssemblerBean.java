package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.CreateEventAssembler;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.common.dto.CreateEventDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.security.InvalidParameterException;

/**
 * Created by cioncag on 03.08.2016.
 */
@Stateless
public class CreateEventAssemblerBean implements CreateEventAssembler {
    private static final Logger LOG = LoggerFactory.getLogger(EventAssemblerBean.class);

    @Override
    public Event dtoToModel(CreateEventDto dto) throws CommonException {
        Event event = null;
        try {
            event = new Event();
            event.setText(dto.getText());
            event.setDateTime(dto.getDateTime());
            event.setTitle(dto.getTitle());
            return event;
        } catch (NullPointerException e) {
            LOG.error("Cannot convert dto to model.", e);
            throw new CommonException("Cannot convert dto to model.", e);
        }

    }

    @Override
    public CreateEventDto modelToDto(Event model) throws CommonException {
        try {
            return null;
        } catch (InvalidParameterException e) {
            LOG.error("Cannot convert model to dto.", e);
            throw new CommonException("Cannot convert model to dto.", e);
        }
    }
}
