package com.season.portal.devices;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class DeviceListPageModel extends PageModel {

    @IGuidValidatorConstrain(required = false)
    private String resellerId;
    @IGuidValidatorConstrain(required = false)
    private String deviceId;

    private String status;
    @IDateValidatorConstrain(required = false)
    private String startCreationDate;
    @IDateValidatorConstrain(required = false)
    private String endCreationDate;

    @IDateValidatorConstrain(required = false)
    private String startActivationDate;
    @IDateValidatorConstrain(required = false)
    private String endActivationDate;

    @IDateValidatorConstrain(required = false)
    private String startExpirationDate;
    @IDateValidatorConstrain(required = false)
    private String endExpirationDate;

    public String getResellerId() {
        return resellerId;
    }

    public void setResellerId(String resellerId) {
        this.resellerId = resellerId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getStartActivationDate() {
        return startActivationDate;
    }

    public void setStartActivationDate(String startActivationDate) {
        this.startActivationDate = startActivationDate;
    }

    public String getEndActivationDate() {
        return endActivationDate;
    }

    public void setEndActivationDate(String endActivationDate) {
        this.endActivationDate = endActivationDate;
    }

    public String getStartExpirationDate() {
        return startExpirationDate;
    }

    public void setStartExpirationDate(String startExpirationDate) {
        this.startExpirationDate = startExpirationDate;
    }

    public String getEndExpirationDate() {
        return endExpirationDate;
    }

    public void setEndExpirationDate(String endExpirationDate) {
        this.endExpirationDate = endExpirationDate;
    }
}
