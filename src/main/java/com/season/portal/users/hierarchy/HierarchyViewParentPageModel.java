package com.season.portal.users.hierarchy;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class HierarchyViewParentPageModel extends PageModel {

    @IGuidValidatorConstrain()
    private String childId;

    private String parentName;

    public HierarchyViewParentPageModel(String childId) {
        this.childId = childId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
