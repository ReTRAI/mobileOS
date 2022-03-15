package com.season.portal.devices;

import com.season.portal.client.generated.device.Device;

import java.util.Date;

public class SimpleDeviceModel {
    private String deviceId;
    private String deviceStatus;
    private String creationDate;
    private String activationDate;
    private String expireDate;
    private String imei;
    public SimpleDeviceModel(Device device) {
        deviceId = device.getDeviceId();
        deviceStatus = device.getDeviceStatus();
        creationDate = device.getCreationDate();
        activationDate = device.getActivationDate();
        expireDate = device.getExpireDate();
        imei = device.getImeiNumber();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
