package com.season.portal.support;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SupportModel {
    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    private String title;

    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    private String msg;

    @NotNull(message = "utils_form_required")
    @NotBlank(message = "utils_form_required")
    private String img;
}
