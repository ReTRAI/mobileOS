package com.season.portal.utils.model;

public class RestStringModel extends RestModel{
    private String result = "";

    public RestStringModel() {

    }

    public void setResult(String result) {
            this.result = result;
    }

    public void setResult(String result, boolean worked) {
        this.result = result;
        setWorked(worked);
    }

    public String getResult() {
        return result;
    }
}
