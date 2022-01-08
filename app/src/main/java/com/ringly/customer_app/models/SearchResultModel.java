package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResultModel {
    @SerializedName("results") private ArrayList<ResultsArray> results;
    @SerializedName("total") private int total;
    @SerializedName("total_pages") private int totalPages;

    public ArrayList<ResultsArray> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultsArray> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
