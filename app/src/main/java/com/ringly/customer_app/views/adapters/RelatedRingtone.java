package com.ringly.customer_app.views.adapters;

public class RelatedRingtone {

    //    private String songTitle;
    private String imgName;
    //    private String mp3url;
    private String imgUrl;
    public boolean isFavourite;

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

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public RelatedRingtone(){

    }

}