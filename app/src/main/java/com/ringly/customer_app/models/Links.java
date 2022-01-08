package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("url")
    private String downloadEndpoint;

    public String getDownloadEndpoint() {
        return downloadEndpoint;
    }

    public void setDownloadEndpoint(String downloadEndpoint) {
        this.downloadEndpoint = downloadEndpoint;
    }
}
