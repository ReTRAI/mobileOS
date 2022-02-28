package com.season.portal.users.hierarchy;

import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

import javax.validation.constraints.NotNull;

public class HierarchyModel {

    @IGuidValidatorConstrain()
    private String parentId;
    @IGuidValidatorConstrain()
    private String childId;

    public HierarchyModel(String parentId, String childId) {
        this.parentId = parentId;
        this.childId = childId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }
}
