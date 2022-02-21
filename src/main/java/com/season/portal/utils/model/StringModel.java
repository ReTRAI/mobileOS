package com.season.portal.utils.model;

public class StringModel extends PageModel {
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

    public StringModel(Integer numPerPage) {
        super(0, numPerPage);
    }

    public StringModel(String value, Integer offset, Integer numPerPage) {
        super(offset, numPerPage);
        this.value = value;
    }
}
