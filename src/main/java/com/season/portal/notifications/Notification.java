package com.season.portal.notifications;

public class Notification {
    private Long guid;
    private String path;
    private String title;
    private String sub;

    public Notification(Long guid, String path, String title, String sub) {
        this.guid = guid;
        this.path = path;
        this.title = title;
        this.sub = sub;
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
