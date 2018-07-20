package edu.msg.jbook.web;

import edu.msg.jbook.common.dto.PostDto;
import edu.msg.jbook.web.controllers.PostController;
import org.primefaces.model.UploadedFile;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by cioncag on 29.07.2016.
 */

@ManagedBean
@RequestScoped
public class PostBean implements Serializable {

    private PostDto createdPost = new PostDto();
    private UploadedFile fileUpload;
    private UploadedFile fileUploadVideo;
    private boolean showProgress = false;
    @EJB
    private PostController pc;

    public PostBean() {
    }

    public void uploadImage() {
        this.showProgress = true;
        if (this.fileUploadVideo != null) {
            if (this.fileUploadVideo.getFileName().length() > 0) {
                uploadVideo();
            }
        }
        if (this.fileUpload != null) {
            if (this.fileUpload.getFileName().length() > 0) {
                ImageUploadDropboxBean.fileUpload = this.fileUpload;
                String imagePath = "";
                try {
                    imagePath = ImageUploadDropboxBean.uploadImage();
                } catch (IOException e) {
                }
                createdPost.setPhoto(imagePath);
            }
        }
        this.showProgress = false;
    }
    public void uploadVideo() {
        ImageUploadDropboxBean.fileUpload = this.fileUploadVideo;
        String videoPath = "";
        try {
            videoPath = ImageUploadDropboxBean.uploadImage();
        } catch (IOException e) {
        }
        createdPost.setVideo(videoPath);
    }

    public void createPost() {
        pc.createPost(createdPost);
        try {
            FacesContext.getCurrentInstance().getExternalContext().
                    redirect("home.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public PostDto getCreatedPost() {
        return createdPost;
    }

    public void setCreatedPost(PostDto createdPost) {
        this.createdPost = createdPost;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public UploadedFile getFileUploadVideo() {
        return fileUploadVideo;
    }

    public void setFileUploadVideo(UploadedFile fileUploadVideo) {
        this.fileUploadVideo = fileUploadVideo;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }
}
