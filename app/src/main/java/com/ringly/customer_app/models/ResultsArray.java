package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

public class ResultsArray {

    @SerializedName("urls") private Urls urls;
    @SerializedName("user") private User user;
    @SerializedName("id") private String id;

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
