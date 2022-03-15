package com.season.portal.balance;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;
import com.season.portal.utils.validation.constrain.INumberValidatorConstrain;

public class BalanceListPageModel extends PageModel {

    @IGuidValidatorConstrain(required = true)
    private String resellerId;

    @IDateValidatorConstrain
    private String startDate;
    @IDateValidatorConstrain
    private String endDate;

    @INumberValidatorConstrain
    private String minVal;
    @INumberValidatorConstrain
    private String maxVal;

    private String debitCredit;

    public String getResellerId() {
        return resellerId;
    }

    public void setResellerId(String resellerId) {
        this.resellerId = resellerId;
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

    public String getMinVal() {
        return minVal;
    }

    public void setMinVal(String minVal) {
        this.minVal = minVal;
    }

    public String getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(String maxVal) {
        this.maxVal = maxVal;
    }

    public String getDebitCredit() {
        return debitCredit;
    }

    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit;
    }

    public String getValidDebitCredit() {
        String result = "";
        switch(debitCredit){
            case "d":
            case "c":
                result = debitCredit;
                break;
        }
        return result;
    }
}
