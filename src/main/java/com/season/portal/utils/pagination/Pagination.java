package com.season.portal.utils.pagination;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Pagination {

    private int totalPages;
    private int actualPage;
    private int numPerPage;



    private int totalElements;
    private ArrayList<Integer> pagination;

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getActualPage() {
        return actualPage;
    }

    public void setActualPage(Integer actualPage) {
        this.actualPage = actualPage;
    }

    public Integer getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(Integer numPerPage) {
        this.numPerPage = numPerPage;
    }

    public ArrayList<Integer> getPagination() {
        return pagination;
    }

    public void setPagination(ArrayList<Integer> pagination) {
        this.pagination = pagination;
    }

    public Pagination(int totalElements, int actualPage, int numPerPage,
                      int paginationNum) {
        this.totalElements = totalElements;

        this.totalPages = (totalElements + numPerPage - 1) / numPerPage;

        this.actualPage = (actualPage < 1)?1:(actualPage > totalPages)?totalPages:actualPage;

        this.numPerPage = numPerPage;

        this.pagination = new ArrayList<>();

        if(this.totalPages > 1){
            int auxOffstet = paginationNum/2;
            int minPage = this.actualPage - auxOffstet;
            minPage = (minPage < 1)? 1: minPage;
            int maxPage = minPage + paginationNum;
            maxPage = (maxPage > this.totalPages)? this.totalPages: maxPage;

            int minPageAux = maxPage-paginationNum;
            if(minPageAux > 0 && minPage > minPageAux){
                minPage = minPageAux;
            }

            for(int i = minPage; i <= maxPage; i++){
                this.pagination.add(i);
            }
        }

    }


    public Integer getOffset(){
        return (actualPage - 1) * numPerPage;
    }

    public String addUrlParam(String urlParams, String key, String value){

        HashMap<String, String> query_pairs = new HashMap<String, String>();
        String[] pairs = urlParams.split("&");
        for (String pair : pairs) {
            String[] splitPair = pair.split("=");
            if(splitPair.length==2){
                query_pairs.put(splitPair[0], splitPair[1]);
            }
        }

        query_pairs.put(key, value);

        urlParams = "";
        for (Map.Entry<String,String> pair : query_pairs.entrySet()) {
            urlParams += pair.getKey()+"="+pair.getValue()+"&";
        }
        return urlParams;
    }
}
