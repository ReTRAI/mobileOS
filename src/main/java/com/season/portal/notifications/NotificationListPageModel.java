package com.season.portal.notifications;

import com.season.portal.utils.Utils;
import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;

import java.util.Locale;

public class NotificationListPageModel extends PageModel {

    @IGuidValidatorConstrain(required = false)
    private String elementGuid;

    @IDateValidatorConstrain(required = false)
    private String startCreationDate;
    @IDateValidatorConstrain(required = false)
    private String endCreationDate;

    @IDateValidatorConstrain(required = false)
    private String startCheckedDate;
    @IDateValidatorConstrain(required = false)
    private String endCheckedDate;

    private String checked;

    public String getElementGuid() {
        return elementGuid;
    }

    public void setElementGuid(String elementGuid) {
        this.elementGuid = elementGuid;
    }

    public String getStartCreationDate() {
        return startCreationDate;
    }

    public void setStartCreationDate(String startCreationDate) {
        this.startCreationDate = startCreationDate;
    }

    public String getEndCreationDate() {
        return endCreationDate;
    }

    public void setEndCreationDate(String endCreationDate) {
        this.endCreationDate = endCreationDate;
    }

    public String getStartCheckedDate() {
        return startCheckedDate;
    }

    public void setStartCheckedDate(String startCheckedDate) {
        this.startCheckedDate = startCheckedDate;
    }

    public String getEndCheckedDate() {
        return endCheckedDate;
    }

    public void setEndCheckedDate(String endCheckedDate) {
        this.endCheckedDate = endCheckedDate;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getValidStartCreationDate() {
        return Utils.strToStrDateTime(startCreationDate);
    }
    public String getValidEndCreationDate() {
        return Utils.strToStrDateTime(endCreationDate);
    }

    public String getValidStartCheckedDate() {
        return Utils.strToStrDateTime(startCheckedDate);
    }
    public String getValidEndCheckedDate() {
        return Utils.strToStrDateTime(endCheckedDate);
    }

    public String getValidChecked() {
        if(checked != null){
            switch(checked.toLowerCase()){
                case "true":
                case "1":
                    return "1";
                case "false":
                case "0":
                    return "0";
            }
        }
        return "";
    }

    public void tryDefaultOrder() {
        if(canDefaultOrder()){
            setOrder("desc");
            setSort("creationDate");
        }
    }
}
