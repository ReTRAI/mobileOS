package com.season.portal.language;

import com.season.portal.utils.validation.IPasswordValidatorConstrain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//DTO (Data Transfer Object)
public class LanguageModel {
    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    private String code;

    public LanguageModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
