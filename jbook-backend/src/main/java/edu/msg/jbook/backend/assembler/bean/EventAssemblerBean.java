package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.EventAssembler;
import edu.msg.jbook.backend.assembler.ProfileAssembler;
import edu.msg.jbook.backend.model.Event;
import edu.msg.jbook.common.dto.EventDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * Created by dogaro on 29/07/2016.
 */
@Stateless
public class EventAssemblerBean implements EventAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(EventAssemblerBean.class);

    @EJB
    private ProfileAssembler profileAssembler;

    @Override
    public EventDto modelToDto(Event model) throws CommonException {
        EventDto dto = null;
        LocalDate localDate = null;
        Date date = null;
        try {
            dto = new EventDto();
            dto.setId(model.getId());
            dto.setAdmin(profileAssembler.modelToDto(model.getUserAdmin()));
            dto.setTitle(model.getTitle());
            dto.setText(model.getText());
            localDate = model.getDateTime().toLocalDate();
            date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            dto.setDateTime(date);
            dto.setCreationTime(model.getCreationTime());
            return dto;
        } catch (NullPointerException | CommonException e) {
            LOG.error("Cannot convert model to dto.", e);
            throw new CommonException("Cannot convert model to dto.", e);
        }
    }

    @Override
    public Event dtoToModel(EventDto dto) throws CommonException {
        Event event = null;
        LocalDateTime dateTime = null;
        try {
            event = new Event();
            event.setId(dto.getId());
            event.setText(dto.getText());
            event.setTitle(dto.getTitle());
            event.setUserAdmin(profileAssembler.dtoToModel(dto.getAdmin()));
            dateTime = LocalDateTime.ofInstant(dto.getDateTime().toInstant(), ZoneId.systemDefault());
            event.setDateTime(dateTime);
            event.setCreationTime(dto.getCreationTime());
            return event;
        } catch (NullPointerException | CommonException e) {
            LOG.error("Cannot convert dto to model.", e);
            throw new CommonException("Cannot convert model to dto.", e);
        }
    }
}
