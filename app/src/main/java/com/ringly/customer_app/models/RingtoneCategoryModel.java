package com.ringly.customer_app.models;

public class RingtoneCategoryModel {
    private String ringtoneCategoryId;
    private String categoryImageUrl;
    private String ringtoneCategory;

    public RingtoneCategoryModel() {
    }

    public RingtoneCategoryModel(String ringtoneCategoryId, String categoryImageUrl, String ringtoneCategory) {
        this.ringtoneCategoryId = ringtoneCategoryId;
        this.categoryImageUrl = categoryImageUrl;
        this.ringtoneCategory = ringtoneCategory;
    }

    public String getRingtoneCategoryId() {
        return ringtoneCategoryId;
    }

    public void setRingtoneCategoryId(String ringtoneCategoryId) {
        this.ringtoneCategoryId = ringtoneCategoryId;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getRingtoneCategory() {
        return ringtoneCategory;
    }

    public void setRingtoneCategory(String ringtoneCategory) {
        this.ringtoneCategory = ringtoneCategory;
    }
}
