package edu.msg.jbook.backend.assembler.bean;

import edu.msg.jbook.backend.assembler.PostAssembler;
import edu.msg.jbook.backend.assembler.SearchAssembler;
import edu.msg.jbook.backend.model.Post;
import edu.msg.jbook.backend.model.Privacy;
import edu.msg.jbook.backend.model.UserState;
import edu.msg.jbook.common.dto.PostDto;
import edu.msg.jbook.common.dto.SearchDto;
import edu.msg.jbook.common.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cioncag on 29.07.2016.
 */
@Stateless
public class PostAssemblerBean implements PostAssembler {
    private static final Logger LOG = LoggerFactory.getLogger(SearchAssemblerBean.class);

    @EJB
    private SearchAssembler searchAssembler;

    @Override
    public Post dtoToModel(PostDto dto) throws CommonException {
        Post post = null;
        try {
            post = new Post();

            if (dto.getText() != null) {
                post.setText(dto.getText());
            }
            if (dto.getPhoto() != null) {
                post.setPhoto(dto.getPhoto());
            }
            if (dto.getVideo() != null) {
                post.setVideo(dto.getVideo());
            }
            if (dto.getPrivacy() != null) {
                switch (dto.getPrivacy()) {
                    case ONLY_ME:
                        post.setPrivacy(Privacy.ONLY_ME);
                        break;
                    case FRIENDS:
                        post.setPrivacy(Privacy.FRIENDS);
                    case FRIENDS_OF_FRIENDS:
                        post.setPrivacy(Privacy.FRIENDS_OF_FRIENDS);
                    case PUBLIC:
                        post.setPrivacy(Privacy.PUBLIC);
                    default:
                        post.setPrivacy(Privacy.PUBLIC);
                }
            }
            return post;
        }catch (NullPointerException e){
            LOG.error("Cannot convert dto to model.",e);
            throw new CommonException("Cannot convert dto to model.",e);
        }
    }

    @Override
    public PostDto modelToDto(Post model) throws CommonException {
        PostDto postDto = null;
        List<SearchDto> likes = null;
        try {
            postDto = new PostDto();
            postDto.setId(model.getId());
            postDto.setOwnerId(model.getOwner().getId());
            postDto.setFirstName(model.getOwner().getUser().getFirstName());
            postDto.setLastName(model.getOwner().getUser().getLastName());
            postDto.setCreationTime(model.getCreationTime());
            likes = new LinkedList<>();
            for (UserState user: model.getUserLikes()) {
                likes.add(searchAssembler.modelToDto(user));
            }
            postDto.setLikes(likes);
            if(model.getPrivacy()!=null) {
                switch (model.getPrivacy()) {
                    case ONLY_ME:
                        postDto.setPrivacy(PostDto.Privacy.ONLY_ME);
                    case FRIENDS:
                        postDto.setPrivacy(PostDto.Privacy.FRIENDS);
                    case FRIENDS_OF_FRIENDS:
                        postDto.setPrivacy(PostDto.Privacy.FRIENDS_OF_FRIENDS);
                    case PUBLIC:
                        postDto.setPrivacy(PostDto.Privacy.PUBLIC);
                    default:

                }
            }else{
                postDto.setPrivacy(PostDto.Privacy.PUBLIC);
            }
            if(model.getPhoto() != null) {
                if (!model.getPhoto().equals("")) {
                    postDto.setPhoto(model.getPhoto());
                    postDto.setRenderImage(true);
                }
            }
            if(model.getVideo() != null) {
                if (!model.getVideo().equals("")) {
                    postDto.setVideo(model.getVideo());
                    postDto.setRenderVideo(true);
                }
            }

            if(model.getText() != null) {
                if (!model.getText().equals("")) {
                    postDto.setText(model.getText());
                    postDto.setRenderText(true);
                }
            }

            if(model.getOwner().getPhoto() != null) {
                if (!model.getOwner().getPhoto().equals("")) {
                    postDto.setUserPhoto(model.getOwner().getPhoto());
                }
            }
            return postDto;
        }catch(NullPointerException | IllegalArgumentException | CommonException e){
            LOG.error("Cannot convert model to dto.",e);
            throw new CommonException("Cannot convert model to dto.",e);
        }
    }
}
