package com.ringly.customer_app.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ringly.customer_app.R;

public class activity_about_us extends AppCompatActivity {
    WebView about;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setContentView(R.layout.activity_about_us);
        about = findViewById(R.id.about);
        WebSettings webSettings = about.getSettings();
        webSettings.setJavaScriptEnabled(true);
        about.loadUrl("file:///android_asset/aboutus.html");
        // getActionBar().setTitle("About Us");
    }
}