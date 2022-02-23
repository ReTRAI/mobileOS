package com.season.portal.reseller;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class ResellerListPageModel extends PageModel {

    @IGuidValidatorConstrain(required = false)
    private String resellerId;
    private String resellerName;
    private boolean onlyChildren;

    public String getResellerId() {
        return resellerId;
    }

    public void setResellerId(String resellerId) {
        this.resellerId = resellerId;
    }

    public String getResellerName() {
        return resellerName;
    }

    public void setResellerName(String resellerName) {
        this.resellerName = resellerName;
    }

    public boolean isOnlyChildren() {
        return onlyChildren;
    }

    public void setOnlyChildren(boolean onlyChildren) {
        this.onlyChildren = onlyChildren;
    }
}
