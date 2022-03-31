package com.season.portal.ticket;

import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewTicketDetailModel {
    public final static int detailMinChar = 5;
    public final static int detailMaxChar = 3000;

    public final static int filesMaxFiles = 2;
    public final static int filesMaxFileSizeMB = 5;

    public final static String[] fileTypeGroups = {"image", "zip"};
    public final static String[] fileTypes = {"pdf"};

    @IGuidValidatorConstrain(required = false)
    private String ticketId;

    @NotNull(message = "utils_form_required")
    @Size(min = detailMinChar, max = detailMaxChar, message = "utils_form_minmaxChar")
    private String detail;

    @IFileValidatorConstrain(required = false, maxSizeMB = filesMaxFileSizeMB, fileTypeGroups={"image", "zip"}, fileTypes = {"pdf"})
    private MultipartFile file;

    public NewTicketDetailModel(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
