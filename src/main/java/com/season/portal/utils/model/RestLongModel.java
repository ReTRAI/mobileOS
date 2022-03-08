package com.season.portal.utils.model;

public class RestLongModel extends RestModel{
    private long result = 0;

    public RestLongModel() {

    }

    public void setResult(long result) {
            this.result = result;
    }
    public void setResult(long result, boolean worked) {
        this.result = result;
        setWorked(worked);
    }

    public long getResult() {
        return result;
    }
}
