package com.season.portal.ticket;

import com.season.portal.client.support.ClientSupport;
import com.season.portal.utils.Utils;
import com.season.portal.utils.validation.constrain.IEnumValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class UpdateTicketStatusModel {
    @IGuidValidatorConstrain(required = true)
    private String ticketId;

    @IEnumValidatorConstrain(required = true, validValues = {"COMPLETED", "PENDING", "ONPROGRESS"})
    private String status;

    public UpdateTicketStatusModel(String ticketId) {
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
}
