package com.season.portal.utils.model;

import com.season.portal.utils.validation.constrain.IFilesValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

import javax.validation.constraints.NotNull;

public class GuidRequiredModel {

    @IGuidValidatorConstrain(required = true)
    @NotNull(message = "utils_form_required")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public GuidRequiredModel() {
        value="";
    }

    public GuidRequiredModel(String value) {
        this.value = value;
    }
}
