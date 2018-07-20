package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.SearchAssembler;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

/**
 * Created by marcp on 29.07.2016.
 */
@Stateless
public class SearchAssemblerBean implements SearchAssembler{
    private static final Logger LOG = LoggerFactory.getLogger(SearchAssemblerBean.class);

    @Override
    public UserState dtoToModel(SearchDto dto) throws CommonException{

        try{
            return null;
        }catch(IllegalArgumentException e){
            LOG.error("Cannot convert dto to model.",e);
            throw new CommonException("Cannot convert dto to model.",e);
        }
    }

    @Override
    public SearchDto modelToDto(UserState model) throws CommonException {
        SearchDto searchDto = null;
        try {
            searchDto = new SearchDto();
            searchDto.setFirstName(model.getUser().getFirstName());
            searchDto.setLastName(model.getUser().getLastName());
            searchDto.setId(model.getId());
            searchDto.setPhoto(model.getPhoto());
            return searchDto;
        }catch(IllegalArgumentException e){
            LOG.error("Cannot convert model to dto.",e);
            throw new CommonException("Cannot convert model to dto.",e);
        }
    }
}
