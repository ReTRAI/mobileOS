package com.season.portal.utils.model;

public class PageModel {

    private Integer offset;

    private Integer numPerPage;

    public PageModel() {
        this.offset = 0;
        this.numPerPage = 10;
    }

    public PageModel(Integer numPerPage) {
        this.offset = 0;
        this.numPerPage = numPerPage;
    }

    public PageModel(Integer offset, Integer numPerPage) {
        this.offset = offset;
        this.numPerPage = numPerPage;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(Integer numPerPage) {
        this.numPerPage = numPerPage;
    }
}

