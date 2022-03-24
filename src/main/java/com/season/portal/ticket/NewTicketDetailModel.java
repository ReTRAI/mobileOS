package com.season.portal.ticket;

import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewTicketDetailModel {
    public final static int titleMinChar = 5;
    public final static int titleMaxChar = 255;
    public final static int detailMinChar = 5;
    public final static int detailMaxChar = 3000;

    public final static int filesMaxFiles = 2;
    public final static int filesMaxFileSizeMB = 5;

    public final static String[] fileTypeGroups = {"image", "zip"};
    public final static String[] fileTypes = {"pdf"};

    @NotNull(message = "utils_form_required")
    @Size(min = detailMinChar, max = detailMaxChar, message = "utils_form_minmaxChar")
    private String detail;

    @NotNull(message = "utils_form_required")
    @Size(min = titleMinChar, max = titleMaxChar, message = "utils_form_minmaxChar")
    private String title;
    /*
      @IFilesValidatorConstrain(required = false, maxSizeMB = filesMaxFileSizeMB, fileTypeGroups={"image"}, maxFiles = filesMaxFiles)
      @NotNull(message = "utils_form_required")
      private List<MultipartFile> files;
      */
    @IFileValidatorConstrain(required = false, maxSizeMB = filesMaxFileSizeMB, fileTypeGroups={"image", "zip"}, fileTypes = {"pdf"})
    private MultipartFile file;


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    /*
    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }*/

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
