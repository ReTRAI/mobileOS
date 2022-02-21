package com.season.portal.users;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;

import javax.validation.constraints.Email;

public class UsersListPageModel extends PageModel {
    private String nickName;

    @Email(message = "utils_form_invalid_email")
    private String email;

    private String status;

    @IDateValidatorConstrain
    private String startDate;

    @IDateValidatorConstrain
    private String endDate;

    public UsersListPageModel() {}

    public UsersListPageModel(Integer numPerPage) {
        super(0, numPerPage);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String state) {
        this.status = state;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
