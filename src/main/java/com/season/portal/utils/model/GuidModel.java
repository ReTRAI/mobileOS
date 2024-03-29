package com.season.portal.utils.model;

import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class GuidModel {
    @IGuidValidatorConstrain(required = false)
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
