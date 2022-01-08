package com.ringly.customer_app.views.adapters;

import com.google.firebase.database.Exclude;

public class RingtoneObject {

    public String imgName, imgUrl;

    @Exclude
    public String id;

    @Exclude
    public String category;

    @Exclude
    public boolean isFavourite = false;

    public RingtoneObject( String imgName, String imgUrl,String id,String category) {
        this.id = id;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
        this.category = category;
    }


    public RingtoneObject(String imgName, String imgUrl) {
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
