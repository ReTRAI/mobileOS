package com.season.portal.ticket;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class TicketListPageModel extends PageModel {

    @IGuidValidatorConstrain(required = false)
    private String ticketId;
    @IGuidValidatorConstrain(required = false)
    private String assignedUserId;
    @IGuidValidatorConstrain(required = false)
    private String openUserId;

    private String ticketStatus;
    @IDateValidatorConstrain(required = false)
    private String startCreationDate;
    @IDateValidatorConstrain(required = false)
    private String endCreationDate;

    public TicketListPageModel() {}

    public TicketListPageModel(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
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

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }

    public void tryDefaultOrder() {
        if(canDefaultOrder()){
            setSort("openDate");
            setOrder("desc");
        }
    }
}
