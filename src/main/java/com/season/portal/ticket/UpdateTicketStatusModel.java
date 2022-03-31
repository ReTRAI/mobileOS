package com.season.portal.ticket;

import com.season.portal.client.support.ClientSupport;
import com.season.portal.utils.Utils;
import com.season.portal.utils.validation.constrain.IEnumValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class UpdateTicketStatusModel {
    public final static String[] TICKET_STATUS_ALLOWED = {"COMPLETED", "PENDING", "ONPROGRESS", "OPEN"};

    @IGuidValidatorConstrain(required = true)
    private String ticketId;

    @IEnumValidatorConstrain(required = true, enumValues = {"COMPLETED", "PENDING", "ONPROGRESS", "OPEN"})
    private String status;

    public UpdateTicketStatusModel(){}

    public UpdateTicketStatusModel(String ticketId) {
        this.ticketId = ticketId;
    }

    public UpdateTicketStatusModel(String ticketId, String status) {
        this.ticketId = ticketId;
        this.status = status;
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
