package com.ringly.customer_app.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ringly.customer_app.R;

public class activity_privacypolicy extends AppCompatActivity {

    WebView privacy_policy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);
        // getSupportActionBar().setTitle("Privacy Policy");
        privacy_policy = findViewById(R.id.privacy);
        WebSettings webSettings = privacy_policy.getSettings();
        webSettings.setJavaScriptEnabled(true);
        privacy_policy.loadUrl("file:///android_asset/privacypolicy.html");
    }
}