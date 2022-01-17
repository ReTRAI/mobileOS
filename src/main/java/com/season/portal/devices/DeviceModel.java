package com.season.portal.devices;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;

//DTO (Data Transfer Object)
public class DeviceModel extends PageModel {
    private int active;
    private int free;
    private int wiped;
    private int blocked;
    private int suspended;

    //Gives big text message if cant convert
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    @IDateValidatorConstrain
    private String startDate;

    @IDateValidatorConstrain
    private String endDate;

    public DeviceModel() {
        this.active = -1;
        this.free = -1;
        this.wiped = -1;
        this.blocked = -1;
        this.suspended = -1;
    }

    public DeviceModel(Integer numPerPage) {
        super(0, numPerPage);
    }


    public int getActive() {
        return active;
    }

    public void setActive(int activate) {
        this.active = activate;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getWiped() {
        return wiped;
    }

    public void setWiped(int wiped) {
        this.wiped = wiped;
    }

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public int getSuspended() {
        return suspended;
    }

    public void setSuspended(int suspended) {
        this.suspended = suspended;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}