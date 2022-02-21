package com.season.portal.devices;

import java.util.Date;

public class Device {
    private String guid;
    private boolean active;
    private boolean free;
    private boolean wiped;
    private boolean blocked;
    private boolean suspended;
    private Date expire;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActivate(boolean active) {
        this.active = active;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isWiped() {
        return wiped;
    }

    public void setWiped(boolean wiped) {
        this.wiped = wiped;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public Device() {

    }
    public Device(String guid, boolean active, boolean free, boolean wiped, boolean blocked, boolean suspended, Date expire) {
        this.guid = guid;
        this.active = active;
        this.free = free;
        this.wiped = wiped;
        this.blocked = blocked;
        this.suspended = suspended;
        this.expire = expire;
    }
}
