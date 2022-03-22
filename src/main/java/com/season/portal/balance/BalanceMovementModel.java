package com.season.portal.balance;

import com.season.portal.utils.model.PageModel;
import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;
import com.season.portal.utils.validation.constrain.INumberValidatorConstrain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.season.portal.client.reseller.ClientReseller.MOVEMENT_CREDIT;
import static com.season.portal.client.reseller.ClientReseller.MOVEMENT_DEBIT;

public class BalanceMovementModel {

    public final static String VALUE_MINVAL = "0";
    public final static int DETAIL_MIN = 3;
    public final static int DETAIL_MAX = 150;

    @IGuidValidatorConstrain(required = true)
    private String resellerId;

    @INumberValidatorConstrain(minVal = VALUE_MINVAL, required = true)
    private String movementValue;

    @NotNull(message = "utils_form_required")
    @Size(min = DETAIL_MIN, max = DETAIL_MAX, message = "utils_form_minmaxChar")
    private String movementDetail;

    private String movementType;
    private String debitCredit;

    public BalanceMovementModel(String resellerId) {
        this.resellerId = resellerId;
    }

    public String getResellerId() {
        return resellerId;
    }

    public void setResellerId(String resellerId) {
        this.resellerId = resellerId;
    }

    public String getMovementValue() {
        return movementValue;
    }

    public void setMovementValue(String movementValue) {
        this.movementValue = movementValue;
    }

    public String getMovementDetail() {
        return movementDetail;
    }

    public void setMovementDetail(String movementDetail) {
        this.movementDetail = movementDetail;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public String getDebitCredit() {
        return debitCredit;
    }

    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit;
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
}
