package com.season.portal.utils.model;

import java.util.HashMap;

public class RestBoolModel extends RestModel{
    private boolean result = false;

    public RestBoolModel() {

    }

    public void setResult(boolean result) {
            this.result = result;
    }
    public void setResult(boolean result, boolean worked) {
        this.result = result;
        setWorked(worked);
    }

    public boolean getResult() {
        return result;
    }
}
