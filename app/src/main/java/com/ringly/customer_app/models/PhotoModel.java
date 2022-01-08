package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

public class PhotoModel {

    @SerializedName("id") public String id;
    @SerializedName("user") private User user;
    @SerializedName("urls") private Urls urls;
    @SerializedName("likes") private int likes;
    @SerializedName("links") private Links links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
