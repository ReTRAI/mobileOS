package com.season.portal.users.hierarchy;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

public class HierarchyViewChildPageModel extends PageModel {

    @IGuidValidatorConstrain()
    private String parentId;

    //To search
    private String childName;

    public HierarchyViewChildPageModel(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildName() {
        return childName;
    }

    public String getValidChildName(){
        return (childName == null)?"":childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }
}
