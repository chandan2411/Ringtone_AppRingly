package com.ringly.customer_app.models;

import com.google.gson.annotations.SerializedName;

public class Urls {
    @SerializedName("regular") private String image_regular;
    @SerializedName("small") private String image_small;
    @SerializedName("raw") private String image_raw;
    @SerializedName("full") private String full;

    public String getImage_regular() {
        return image_regular;
    }

    public void setImage_regular(String image_regular) {
        this.image_regular = image_regular;
    }

    public String getImage_small() {
        return image_small;
    }

    public void setImage_small(String image_small) {
        this.image_small = image_small;
    }

    public String getImage_raw() {
        return image_raw;
    }

    public void setImage_raw(String image_raw) {
        this.image_raw = image_raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }
}
