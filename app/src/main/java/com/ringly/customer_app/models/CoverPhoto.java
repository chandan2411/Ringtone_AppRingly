package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

public class CoverPhoto {

    @SerializedName("urls")
    private Urls urls;

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }
}
