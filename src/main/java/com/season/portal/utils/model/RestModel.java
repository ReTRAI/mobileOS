package com.season.portal.utils.model;

import java.util.ArrayList;
import java.util.HashMap;

public class RestModel {
    private boolean worked;
    private ArrayList<String> errorKeys = new ArrayList<>();
    private ArrayList<String> successKeys = new ArrayList<>();

    public RestModel() {
        worked = false;
    }

    public RestModel(boolean worked) {
        this.worked = worked;
    }
    public void setWorked(boolean worked) {
        this.worked = worked;
    }

    public void addErrorKeys(ArrayList<String> errorKeys) {
        if(errorKeys != null)
            this.errorKeys.addAll(errorKeys);
    }
    public void addSuccessKeys(ArrayList<String> successKeys) {
        if(successKeys != null)
            this.successKeys.addAll(successKeys);
    }

    public void addErrorKey(String key) {
        this.errorKeys.add(key);
    }


    public boolean isWorked() {
        return worked;
    }

    public ArrayList<String> getErrorKeys() {
        return errorKeys;
    }

    public ArrayList<String> getSuccessKeys() {
        return successKeys;
    }
}
