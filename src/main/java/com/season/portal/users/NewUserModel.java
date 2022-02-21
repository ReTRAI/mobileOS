package com.season.portal.users;

import com.season.portal.utils.validation.constrain.IPasswordValidatorConstrain;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NewUserModel {
    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    @Email(message = "utils_form_invalid_email")
    private String email;

    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    private String nickname;

    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    @IPasswordValidatorConstrain
    private String pass;

    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    @IPasswordValidatorConstrain
    private String checkPass;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCheckPass() {
        return checkPass;
    }

    public void setCheckPass(String checkPass) {
        this.checkPass = checkPass;
    }
}
