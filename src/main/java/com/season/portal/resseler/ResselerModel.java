package com.season.portal.resseler;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.IPasswordValidatorConstrain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//DTO (Data Transfer Object)
public class ResselerModel extends PageModel {
    private String name;

}
