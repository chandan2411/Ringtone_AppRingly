package com.ringly.customer_app.views.adapters;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FavRingtoneModel {

    private String imgName, imgUrl/*, imgId*/;

    /*public FavRingtoneModel(String imgName, String imgUrl, String imgId) {
        this.imgName = imgName;
        this.imgUrl = imgUrl;
        this.imgId = imgId;
    }
*/
    FavRingtoneModel(String imgName, String imgUrl) {
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
