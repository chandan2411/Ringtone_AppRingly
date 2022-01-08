package com.ringly.customer_app.entities;

/**
 * Created by sandeep HR on 03/05/19.
 */
public interface OnMediaDownloadListener {

//    void  onMediaDownload(int type, String savedPath, String name);
    void onMediaDownload(int type, String savedPath, String name, int downloadType);

}
