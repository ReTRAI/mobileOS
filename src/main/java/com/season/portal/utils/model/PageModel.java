package com.season.portal.utils.model;

import java.util.Locale;

public class PageModel {

    private Integer page;
    private Integer numPerPage;
    private String sort;
    private String order;

    public PageModel() {
        this.page = 1;
        this.numPerPage = 10;
        this.order = "desc";
    }

    public PageModel(Integer numPerPage) {
        this.page = 1;
        this.numPerPage = numPerPage;
        this.order = "desc";
    }

    public PageModel(Integer page, Integer numPerPage) {
        this.page = page;
        this.numPerPage = numPerPage;
        this.order = "desc";
    }

    public boolean canDefaultOrder(){
        boolean can = false;
        if(sort == null || sort.equals("")){
          can = true;
        }
        return can;
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
    public String getValidSort() {
        if(sort == null)
            return "";
        return sort;
    }

    public Integer getValidNumPerPage() {
        if(numPerPage < 1)
            return 1;
        return numPerPage;
    }

    public String getValidOrder() {
        String validOder = "asc";

        if(order != null){
            switch (order.toLowerCase(Locale.ROOT)){
                case "asc":
                case "desc":
                    validOder = order;
                    break;
            }
        }

        return validOder;
    }

    public Integer getValidOffset(){
        Integer offset = (page-1) * numPerPage;
        if(offset<0)
            offset = 0;
        return offset;
    }


}

