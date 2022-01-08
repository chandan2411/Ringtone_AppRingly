package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

public class FeaturedCollectionModel {

    @SerializedName("id") private int id;
    @SerializedName("total_photos") private int total_photos;
    @SerializedName("title") private String title;
    @SerializedName("user") private User user;
    @SerializedName("cover_photo") private CoverPhoto coverPhoto;
    @SerializedName("curated") private boolean isCurated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_photos() {
        return total_photos;
    }

    public void setTotal_photos(int total_photos) {
        this.total_photos = total_photos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CoverPhoto getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(CoverPhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public boolean isCurated() {
        return isCurated;
    }

    public void setCurated(boolean curated) {
        isCurated = curated;
    }
}
