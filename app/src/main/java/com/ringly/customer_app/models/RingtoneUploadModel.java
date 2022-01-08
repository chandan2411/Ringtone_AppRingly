package com.ringly.customer_app.models;

/**
 * Created by RAJ ARYAN on 2020-01-14.
 */
public class RingtoneUploadModel {

    private String ringtoneId;

    private String userId;

    private String ringtoneName;

    private String ringtoneLink;

    private String ringtoneCategory;

    private String ringtoneCategoryId;

    private String ringtoneCreatedDate;

    private String ringtoneModifiedDate;

    private Integer ringtoneDownloadCount=0;

    private Integer ringtoneUsedCount=0;

    private Integer ringtoneUsedAsContactToneCount =0;

    private Integer ringtoneUsedAsAlarmToneCount =0;

    private Integer ringtoneUsedAsFavourite =0;

    private int ringtoneDuration =0;

    public RingtoneUploadModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRingtoneDuration() {
        return ringtoneDuration;
    }

    public void setRingtoneDuration(int ringtoneDuration) {
        this.ringtoneDuration = ringtoneDuration;
    }

    public String getRingtoneId() {
        return ringtoneId;
    }

    public void setRingtoneId(String ringtoneId) {
        this.ringtoneId = ringtoneId;
    }

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }

    public String getRingtoneLink() {
        return ringtoneLink;
    }

    public void setRingtoneLink(String ringtoneLink) {
        this.ringtoneLink = ringtoneLink;
    }

    public String getRingtoneCategory() {
        return ringtoneCategory;
    }

    public void setRingtoneCategory(String ringtoneCategory) {
        this.ringtoneCategory = ringtoneCategory;
    }

    public String getRingtoneCategoryId() {
        return ringtoneCategoryId;
    }

    public void setRingtoneCategoryId(String ringtoneCategoryId) {
        this.ringtoneCategoryId = ringtoneCategoryId;
    }

    public String getRingtoneCreatedDate() {
        return ringtoneCreatedDate;
    }

    public void setRingtoneCreatedDate(String ringtoneCreatedDate) {
        this.ringtoneCreatedDate = ringtoneCreatedDate;
    }

    public String getRingtoneModifiedDate() {
        return ringtoneModifiedDate;
    }

    public void setRingtoneModifiedDate(String ringtoneModifiedDate) {
        this.ringtoneModifiedDate = ringtoneModifiedDate;
    }

    public Integer getRingtoneDownloadCount() {
        return ringtoneDownloadCount;
    }

    public void setRingtoneDownloadCount(Integer ringtoneDownloadCount) {
        this.ringtoneDownloadCount = ringtoneDownloadCount;
    }

    public Integer getRingtoneUsedCount() {
        return ringtoneUsedCount;
    }

    public void setRingtoneUsedCount(Integer ringtoneUsedCount) {
        this.ringtoneUsedCount = ringtoneUsedCount;
    }

    public Integer getRingtoneUsedAsContactToneCount() {
        return ringtoneUsedAsContactToneCount;
    }

    public void setRingtoneUsedAsContactToneCount(Integer ringtoneUsedAsContactToneCount) {
        this.ringtoneUsedAsContactToneCount = ringtoneUsedAsContactToneCount;
    }

    public Integer getRingtoneUsedAsAlarmToneCount() {
        return ringtoneUsedAsAlarmToneCount;
    }

    public void setRingtoneUsedAsAlarmToneCount(Integer ringtoneUsedAsAlarmToneCount) {
        this.ringtoneUsedAsAlarmToneCount = ringtoneUsedAsAlarmToneCount;
    }

    public Integer getRingtoneUsedAsFavourite() {
        return ringtoneUsedAsFavourite;
    }

    public void setRingtoneUsedAsFavourite(Integer ringtoneUsedAsFavourite) {
        this.ringtoneUsedAsFavourite = ringtoneUsedAsFavourite;
    }
}
