package com.ringly.customer_app.models;

public class WallPaperModel {
    private String wallPaperId;
    private String wallPaperFullImageUrl;
    private String uploadLocation;
    private String uploaderName;

    public WallPaperModel() {
    }

    public void setWallPaperId(String wallPaperId) {
        this.wallPaperId = wallPaperId;
    }

    public void setWallPaperFullImageUrl(String wallPaperFullImageUrl) {
        this.wallPaperFullImageUrl = wallPaperFullImageUrl;
    }

    public void setUploadLocation(String uploadLocation) {
        this.uploadLocation = uploadLocation;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public WallPaperModel(String wallPaperId, String wallPaperFullImageUrl, String uploaderName, String uploadLocation) {
        this.wallPaperId = wallPaperId;
        this.wallPaperFullImageUrl = wallPaperFullImageUrl;
        this.uploaderName = uploaderName;
        this.uploadLocation = uploadLocation;
    }

    public String getUploadLocation() {
        return uploadLocation;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public String getWallPaperId() {
        return wallPaperId;
    }

    public String getWallPaperFullImageUrl() {
        return wallPaperFullImageUrl;
    }
}
