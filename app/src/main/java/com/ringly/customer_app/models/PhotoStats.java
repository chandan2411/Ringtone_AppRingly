package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

public class PhotoStats {
    @SerializedName("downloads") private DownloadsModel downloadsModel;
    @SerializedName("views") private ViewsModel viewsModel;
    @SerializedName("likes") private LikesModel likesModel;

    public DownloadsModel getDownloadsModel() {
        return downloadsModel;
    }

    public void setDownloadsModel(DownloadsModel downloadsModel) {
        this.downloadsModel = downloadsModel;
    }

    public ViewsModel getViewsModel() {
        return viewsModel;
    }

    public void setViewsModel(ViewsModel viewsModel) {
        this.viewsModel = viewsModel;
    }

    public LikesModel getLikesModel() {
        return likesModel;
    }

    public void setLikesModel(LikesModel likesModel) {
        this.likesModel = likesModel;
    }
}
