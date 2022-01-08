package com.ringly.customer_app.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ringly.customer_app.R;
import com.ringly.customer_app.views.adapters.RingtoneView;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    public static String urlImage;

    private int frag_id;
    public static String search_term;
    private Toolbar toolbar;
    private Context mContext = SecondActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: creating the layout");
        setContentView(R.layout.adapter_ringtone_detail);


        getIntentData();  //receiving intent data

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {  //toolbar navigate button click
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        new RingtoneView();
    }

    private void getIntentData() {
        Log.d(TAG, "getIntentData: getting the intent-received data");

        Intent intent = getIntent();

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: System default back function");
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
