package com.season.portal.reseller;

public class Reseller {
    private Long guid;
    private String name;
    private float balance;
    private int devices;
    private int activated;
    private int notActivated;
    private int free;
    private int wiped;

    public Reseller(Long guid, String name, float balance, int devices, int activated, int notActivated, int free, int wiped) {
        this.guid = guid;
        this.name = name;
        this.balance = balance;
        this.devices = devices;
        this.activated = activated;
        this.notActivated = notActivated;
        this.free = free;
        this.wiped = wiped;
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getDevices() {
        return devices;
    }

    public void setDevices(int devices) {
        this.devices = devices;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public int getNotActivated() {
        return notActivated;
    }

    public void setNotActivated(int notActivated) {
        this.notActivated = notActivated;
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
}
