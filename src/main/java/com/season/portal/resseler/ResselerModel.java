package com.season.portal.resseler;

import com.season.portal.utils.validation.IPasswordValidatorConstrain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//DTO (Data Transfer Object)
public class ResselerModel {
    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    private String name;

}
