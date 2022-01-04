package com.season.portal.utils.model;

import java.util.ArrayList;
import java.util.HashMap;

public class RestHashMapModel extends RestModel{
    private HashMap<String, String> result = new HashMap<>();

    public RestHashMapModel() {

    }

    public void setResult(HashMap<String, String> result) {
            this.result = result;
    }
    public void setResult(HashMap<String, String> result, boolean worked) {
        this.result = result;
        setWorked(worked);
    }

    public HashMap<String, String> getResult() {
        return result;
    }
}
