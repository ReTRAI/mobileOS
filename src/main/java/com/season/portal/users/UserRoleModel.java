package com.season.portal.users;

import com.season.portal.client.generated.reseller.GetResellerParentByChildIdResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.GetSupportParentByChildIdResponse;
import com.season.portal.client.generated.support.Support;
import com.season.portal.utils.validation.constrain.IPasswordValidatorConstrain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRoleModel {
    private String userId;

    private String elementId;

    private String name;

    private boolean empty;

    public UserRoleModel() {
        empty = true;
    }
    public UserRoleModel(Reseller r) {
        empty = true;
        setReseller(r);
    }

    public UserRoleModel(Support s) {
        empty = true;
        setSupport(s);
    }

    public UserRoleModel(GetResellerParentByChildIdResponse response) {
        empty = true;
        if(response != null){
            setReseller(response.getReseller());
        }
    }

    public UserRoleModel(GetSupportParentByChildIdResponse response) {
        empty = true;
        if(response != null){
            setSupport(response.getSupport());
        }
    }

    private void setSupport(Support s){
        if(s != null){
            this.userId = s.getUserId();
            this.elementId = s.getSupportId();
            this.name = s.getSupportName();
            empty = false;
        }
    }

    private void setReseller(Reseller r){
        if(r != null){
            this.userId = r.getUserId();
            this.elementId = r.getResellerId();
            this.name = r.getResellerName();
            empty = false;
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getElementId() {
        return elementId;
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return empty;
    }
}
