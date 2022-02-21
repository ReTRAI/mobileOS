package com.season.portal.auth;

import com.season.portal.utils.validation.constrain.IPasswordValidatorConstrain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChangePassModel {
    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    @IPasswordValidatorConstrain
    private String newPass;

    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    @IPasswordValidatorConstrain
    private String checkPass;

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getCheckPass() {
        return checkPass;
    }

    public void setCheckPass(String checkPass) {
        this.checkPass = checkPass;
    }
}
