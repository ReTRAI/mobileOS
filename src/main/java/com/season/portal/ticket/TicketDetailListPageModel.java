package com.season.portal.ticket;

import com.season.portal.utils.Utils;
import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class TicketDetailListPageModel extends PageModel {

    @IGuidValidatorConstrain(required = true)
    private String ticketId;
    @IGuidValidatorConstrain(required = false)
    private String responseUserId;


    @IDateValidatorConstrain(required = false)
    private String startCreationDate;
    @IDateValidatorConstrain(required = false)
    private String endCreationDate;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getStartCreationDate() {
        return startCreationDate;
    }

    public void setStartCreationDate(String startCreationDate) {
        this.startCreationDate = startCreationDate;
    }

    public String getEndCreationDate() {
        return endCreationDate;
    }

    public void setEndCreationDate(String endCreationDate) {
        this.endCreationDate = endCreationDate;
    }

    public String getResponseUserId() {
        return responseUserId;
    }

    public void setResponseUserId(String responseUserId) {
        this.responseUserId = responseUserId;
    }

    public String getValidStartCreationDate() {
        return Utils.strToStrDateTime(startCreationDate);
    }
    public String getValidEndCreationDate() {
        return Utils.strToStrDateTime(endCreationDate);
    }

    public String getValidResponseUserId() {
        return (responseUserId == null)?"":responseUserId;
    }
}
