package com.season.portal.utils.model;

public class StringPageModel extends PageModel {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringPageModel() {
        this.value = "";
    }

    public StringPageModel(String value) {
        this.value = value;
    }

    public StringPageModel(Integer numPerPage) {
        super(0, numPerPage);
    }

    public StringPageModel(String value, Integer offset, Integer numPerPage) {
        super(offset, numPerPage);
        this.value = value;
    }
}
