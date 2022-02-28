package com.season.portal.support;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class SupportListPageModel extends PageModel {

    @IGuidValidatorConstrain(required = false)
    private String supportId;
    private String supportName;
    private boolean onlyChildren;

    public SupportListPageModel(){super();}

    public SupportListPageModel(String supportId, boolean onlyChildren){
        super();
        this.supportId = supportId;
        this.onlyChildren = onlyChildren;
    }

    public String getSupportId() {
        return supportId;
    }

    public void setSupportId(String supportId) {
        this.supportId = supportId;
    }

    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }

    public boolean isOnlyChildren() {
        return onlyChildren;
    }

    public void setOnlyChildren(boolean onlyChildren) {
        this.onlyChildren = onlyChildren;
    }
}
