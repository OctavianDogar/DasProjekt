package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.CommentAssembler;
import edu.msg.jbook.backend.model.Post;
import edu.msg.jbook.common.dto.CommentDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.security.InvalidParameterException;

/**
 * Created by cioncag on 03.08.2016.
 */
@Stateless
public class CommentAssemblerBean implements CommentAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(EventAssemblerBean.class);

    @Override
    public Post dtoToModel(CommentDto dto) throws CommonException {
        try {
            return null;
        } catch (InvalidParameterException e) {
            LOG.error("Cannot convert dto to model.", e);
            throw new CommonException("Cannot convert dto to model.", e);
        }
    }

    @Override
    public CommentDto modelToDto(Post post) throws CommonException {
        CommentDto commentDto = null;
        try {
            commentDto = new CommentDto();
            commentDto.setId(post.getId());
            commentDto.setUserId(post.getOwner().getId());
            commentDto.setCommentText(post.getText());
            commentDto.setFirstname(post.getOwner().getUser().getFirstName());
            commentDto.setLastname(post.getOwner().getUser().getLastName());
            commentDto.setImagePath(post.getOwner().getPhoto());
            if (post.getParentOfComment() != null)
                commentDto.setParentId(post.getParentOfComment().getId());
            return commentDto;
        } catch (NullPointerException e) {
            LOG.error("Cannot convert model to dto.", e);
            throw new CommonException("Cannot convert model to dto", e);
        }

    }
}
