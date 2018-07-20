package edu.msg.jbook.web;

import com.dropbox.core.*;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created by cioncag on 02.08.2016.
 */
@ManagedBean
@RequestScoped
public class ImageUploadDropboxBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public static UploadedFile fileUpload;
    private final static String DROP_BOX_APP_KEY = "zag0g2t7z51sff6";
    private final static String DROP_BOX_APP_SECRET = "awdhhrmzpsx7h29";

    public ImageUploadDropboxBean() {
    }

    public static String uploadImage() throws IOException {
        String sharedUrl = "";
        DbxAuthFinish authFinish = null;
        String urlPath = "/" + getCurrentUser() + "/" + fileUpload.getFileName();
        DbxAppInfo dbxAppInfo = new DbxAppInfo(DROP_BOX_APP_KEY, DROP_BOX_APP_SECRET);
        DbxRequestConfig reqConfig = new DbxRequestConfig("javarootsDropbox/1.0",
                Locale.getDefault().toString());
        String accessToken = "AwG2Zru3EbAAAAAAAAAAErdZ2Y-BHNA7VNf5I_qwA3vvvskHgCOamxRdEfmGbBZ5";
        DbxClient client = new DbxClient(reqConfig, accessToken);
        InputStream inputStream = fileUpload.getInputstream();
        try {
            DbxEntry.File uploadedFile = client.uploadFile(urlPath, DbxWriteMode.add(), fileUpload.getSize(), inputStream);
            sharedUrl = client.createShareableUrl(urlPath);
            System.out.println("Uploaded: " + uploadedFile.toString() + " URL " + sharedUrl);
        } catch(DbxException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Problem when uploading the image on Dropbox!"));
        } finally {
            inputStream.close();
        }
        sharedUrl = sharedUrl.replace("www.dropbox", "dl.dropboxusercontent");
        return sharedUrl;
    }

    public static String getCurrentUser() {
        HttpSession session = (HttpSession) FacesContext
                .getCurrentInstance().getExternalContext()
                .getSession(false);
        return (String) session.getAttribute("email");
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }
}
