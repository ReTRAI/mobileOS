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

    private long totalElements;
    private ArrayList<Integer> pagination;

    public long getTotalElements() {
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

    public Pagination(long totalElements, int actualPage, int numPerPage,
                      int paginationNum) {
        this.totalElements = totalElements;

        this.totalPages = (int) ((totalElements + numPerPage - 1) / numPerPage);

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

    private HashMap<String, String> urlParamsToHashMap(String urlParams){
        HashMap<String, String> query_pairs = new HashMap<String, String>();
        if(urlParams != null && !urlParams.equals("")){
            String[] pairs = urlParams.split("&");
            for (String pair : pairs) {
                String[] splitPair = pair.split("=");
                if(splitPair.length==2){
                    query_pairs.put(splitPair[0], splitPair[1]);
                }
            }
        }
        return query_pairs;
    }

    public String addUrlParam(String urlParams, String key, String value){

        HashMap<String, String> query_pairs = urlParamsToHashMap(urlParams);
        query_pairs.put(key, value);

        urlParams = "";
        for (Map.Entry<String,String> pair : query_pairs.entrySet()) {
            urlParams += pair.getKey()+"="+pair.getValue()+"&";
        }
        return urlParams;
    }

    public String getSortUrl(String urlBase, String urlParams, String sortName){

        HashMap<String, String> query_pairs = urlParamsToHashMap(urlParams);

        String order = "asc";
        String oldSort  = query_pairs.get("sort");


        if(oldSort != null && oldSort.equals(sortName)){
            String oldOrder = query_pairs.get("order");

            if(oldOrder != null && oldOrder.equals("asc")){
                order="desc";
            }
        }

        query_pairs.put("sort", sortName);
        query_pairs.put("order", order);
        query_pairs.put("page", "1");

        urlParams = "";

        for (Map.Entry<String,String> pair : query_pairs.entrySet()) {
            urlParams += pair.getKey()+"="+pair.getValue()+"&";
        }
        return urlBase+'?'+urlParams;
    }

    public String getSortClass(String urlParams, String sortName){

        HashMap<String, String> query_pairs = urlParamsToHashMap(urlParams);

        String oldSort=query_pairs.get("sort");

        if(oldSort != null && oldSort.equals(sortName)){
            String order = query_pairs.get("order");

            if(order != null && order.equals("asc")){
                return "sorting_asc";
            }
            else{
                return "sorting_desc";
            }
        }
        return "";
    }

}
