package com.season.portal.support;

import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import com.season.portal.utils.validation.constrain.IFilesValidatorConstrain;
import com.season.portal.utils.validation.constrain.ILangCodeValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class TicketModel {
    public final static int titleMinChar = 5;
    public final static int titleMaxChar = 150;
    public final static int detailMinChar = 5;
    public final static int detailMaxChar = 1500;

    public final static int filesMaxFiles = 2;
    public final static int filesMaxFileSizeMB = 5;

    @Size(min = titleMinChar, max = titleMaxChar, message = "utils_form_minmaxChar")
    private String title;

    @Size(min = detailMinChar, max = detailMaxChar, message = "utils_form_minmaxChar")
    private String detail;

    @IFilesValidatorConstrain(required = true, maxSizeMB = filesMaxFileSizeMB, fileTypeGroups={"image"}, maxFiles = filesMaxFiles)
    @NotNull(message = "utils_form_required")
    private List<MultipartFile> files;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
