package com.season.portal.reseller;

import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.utils.model.RestModel;

import java.util.ArrayList;

public class RestResellerListModel extends RestModel {
    private ArrayList<Reseller> result = new ArrayList<Reseller>();

    public RestResellerListModel() {

    }

    public void setResult(ArrayList<Reseller>  result) {
            this.result = result;
    }
    public void setResult(ArrayList<Reseller>  result, boolean worked) {
        this.result = result;
        setWorked(worked);
    }

    public ArrayList<Reseller>  getResult() {
        return result;
    }
}
