package com.ringly.customer_app.entities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahiti on 19/01/17.
 */

public class PermissionClass {

    private PermissionClass() {
        /*
        private constructor
         */
    }

    public static boolean checkPermission(Activity activity) {

        int externalRead = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int externalWrite = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int internet = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int networkstate = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE);
        int readContact = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        int writeContact = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS);

        return externalRead == PackageManager.PERMISSION_GRANTED &&
                externalWrite == PackageManager.PERMISSION_GRANTED &&
                internet == PackageManager.PERMISSION_GRANTED &&
                readContact == PackageManager.PERMISSION_GRANTED &&
                writeContact == PackageManager.PERMISSION_GRANTED &&
                networkstate == PackageManager.PERMISSION_GRANTED ;

    }

    public static void requestPermission(Activity activity) {

        List<String> list = new ArrayList();
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET))
            list.add(Manifest.permission.INTERNET);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_NETWORK_STATE))
            list.add(Manifest.permission.ACCESS_NETWORK_STATE);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS))
            list.add(Manifest.permission.READ_CONTACTS);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CONTACTS))
            list.add(Manifest.permission.WRITE_CONTACTS);

        String[] stockArr = new String[list.size()];
        stockArr = list.toArray(stockArr);
        if (stockArr.length != 0) {
            ActivityCompat.requestPermissions(activity, stockArr, 1);
        }

    }

    public static boolean writePermission(Activity context){
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        return permission;
    }





}
