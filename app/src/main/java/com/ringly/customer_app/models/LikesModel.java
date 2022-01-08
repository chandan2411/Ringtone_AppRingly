package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

public class LikesModel {
    @SerializedName("total")
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
