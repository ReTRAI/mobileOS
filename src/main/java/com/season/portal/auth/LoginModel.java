package com.season.portal.auth;

import com.season.portal.utils.validation.IPasswordValidatorConstrain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//DTO (Data Transfer Object)
public class LoginModel {
    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    @Email(message = "utils_form_invalid_email")
    private String email;

    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    @IPasswordValidatorConstrain
    private String password;

    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    private String langCode;

    public LoginModel() {
        langCode = "pt";
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
