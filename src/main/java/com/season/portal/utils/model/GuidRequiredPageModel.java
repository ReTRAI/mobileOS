package com.season.portal.utils.model;

import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

import javax.validation.constraints.NotNull;

public class GuidRequiredPageModel {

    @IGuidValidatorConstrain(required = true)
    @NotNull(message = "utils_form_required")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public GuidRequiredPageModel() {
        value="";
    }

    public GuidRequiredPageModel(String value) {
        this.value = value;
    }

}
