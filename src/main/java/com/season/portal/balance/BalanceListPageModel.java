package com.season.portal.balance;

import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;
import com.season.portal.utils.validation.constrain.INumberValidatorConstrain;

import static com.season.portal.client.reseller.ClientReseller.MOVEMENT_CREDIT;
import static com.season.portal.client.reseller.ClientReseller.MOVEMENT_DEBIT;

public class BalanceListPageModel extends PageModel {

    public final static String MIN_MINVAL = "0";
    public final static String MAX_MINVAL = "0";

    @IGuidValidatorConstrain(required = false)
    private String resellerId;

    @IDateValidatorConstrain
    private String startDate;
    @IDateValidatorConstrain
    private String endDate;

    @INumberValidatorConstrain(minVal = MIN_MINVAL)
    private String minVal;
    @INumberValidatorConstrain(minVal = MAX_MINVAL)
    private String maxVal;

    private String debitCredit;
    private String movementType;

    public BalanceListPageModel() {
        setOrder("desc");
        setSort("movementDate");
    }

    public BalanceListPageModel(String resellerId) {
        this.resellerId = resellerId;
        setOrder("desc");
        setSort("movementDate");
    }


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

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public String getValidDebitCredit() {
        String result = "";
        if(debitCredit != null){
            debitCredit = debitCredit.toUpperCase();
            switch(debitCredit){
                case MOVEMENT_DEBIT:
                case MOVEMENT_CREDIT:
                    result = debitCredit;
                    break;
            }
        }

        return result;
    }

    public String getValidMovementType(){
        String result = "";
        String a = ClientReseller.MOVEMENT_TYPE.MANUAL_CREDIT.toString();
        if(movementType != null){
            movementType = movementType.toUpperCase();
            for (ClientReseller.MOVEMENT_TYPE mt : ClientReseller.MOVEMENT_TYPE.values()) {
                if(movementType.equals(mt.name())){
                    result = movementType;
                    break;
                }
            }
        }

        return result;
    }
}
