package com.season.portal.utils.model;

public class GuidModel {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public GuidModel() {
        value="";
    }

    public GuidModel(String value) {
        this.value = value;
    }
}
