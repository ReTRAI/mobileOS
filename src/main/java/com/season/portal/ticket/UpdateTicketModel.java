package com.season.portal.ticket;

import com.season.portal.client.support.ClientSupport;
import com.season.portal.utils.Utils;
import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateTicketModel {
    @IGuidValidatorConstrain(required = true)
    private String ticketId;

    private String status;

    private String assignedUserId;

    public UpdateTicketModel(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getValidStatus() {
        String result = "";
        if(status != null){
            String statusUpper = status.toUpperCase();
            for(ClientSupport.TICKET_STATUS enumStatus : ClientSupport.TICKET_STATUS.values()){
                if(statusUpper.equals(enumStatus.name())){
                    result = enumStatus.name();
                    break;
                }
            }
        }

        return result;
    }

    public String getValidAssignedUserId() {
        String result = "";
        if(assignedUserId != null){
            if(Utils.isGuid(assignedUserId)){
                result = assignedUserId;
            }
        }
        return result;
    }
}
