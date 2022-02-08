package com.season.portal.devices;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;

//DTO (Data Transfer Object)
public class DevicePageModel extends PageModel {
    /*
    private int active;
    private int free;
    private int wiped;
    private int blocked;
    private int suspended;
    */
    private String state;

    //Gives big text message if cant convert
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    @IDateValidatorConstrain
    private String startDate;

    @IDateValidatorConstrain
    private String endDate;

    public DevicePageModel() {
        this.state = "";
        /*this.active = -1;
        this.free = -1;
        this.wiped = -1;
        this.blocked = -1;
        this.suspended = -1;*/
    }

    public DevicePageModel(Integer numPerPage) {
        super(0, numPerPage);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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