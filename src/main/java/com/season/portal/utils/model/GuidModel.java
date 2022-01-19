package com.season.portal.utils.model;

public class GuidModel {
    private Long value;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public GuidModel() {
        value=-1l;
    }

    public GuidModel(Long value) {
        this.value = value;
    }
}
