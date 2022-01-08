package com.ringly.customer_app.entities;

/**
 * Created by RAJ ARYAN on 06/05/19.
 */
public class FileModel {
    private  String fileName;
    private  String fileId;
    private  String fileUrl;
    private String position;
    private Integer downloadCount;

    public FileModel(String fileName, String fileId, String fileUrl, String position) {
        this.fileName = fileName;
        this.fileId = fileId;
        this.fileUrl = fileUrl;
        this.position = position;
    }

    public FileModel(String fileName, String fileId, String fileUrl, int downloadCount) {
        this.fileName = fileName;
        this.fileId = fileId;
        this.fileUrl = fileUrl;
        this.downloadCount = downloadCount;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getPosition() {
        return position;
    }
}
