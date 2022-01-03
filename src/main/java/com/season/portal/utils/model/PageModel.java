package com.season.portal.utils.model;

public class PageModel {

    private Integer page;
    private Integer numPerPage;
    private String sort;
    private String order;




    public PageModel() {
        this.page = 1;
        this.numPerPage = 10;
    }

    public PageModel(Integer numPerPage) {
        this.page = 1;
        this.numPerPage = numPerPage;
    }

    public PageModel(Integer page, Integer numPerPage) {
        this.page = page;
        this.numPerPage = numPerPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(Integer numPerPage) {
        this.numPerPage = numPerPage;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}

