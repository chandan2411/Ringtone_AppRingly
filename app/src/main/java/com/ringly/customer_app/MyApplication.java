package com.ringly.customer_app;

import android.app.Application;

import com.downloader.PRDownloader;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializePrDownloader();
    }

    private void initializePrDownloader() {
        PRDownloader.initialize(getApplicationContext());
    }
}
