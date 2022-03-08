package com.season.portal.utils.model;

public class StringModel{
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringModel() {
        this.value = "";
    }

    public StringModel(String value) {
        this.value = value;
    }
}
