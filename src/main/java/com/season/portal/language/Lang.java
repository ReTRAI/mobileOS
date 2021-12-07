package com.season.portal.language;

import java.util.UUID;

public class Lang {
    private UUID id;
    private String code;

    public Lang() {
    }

    public Lang(String code) {
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
