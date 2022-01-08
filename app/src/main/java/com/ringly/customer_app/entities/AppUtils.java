package com.ringly.customer_app.entities;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.ringly.customer_app.R;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by RAJ ARYAN on 2019-12-03.
 */
public class AppUtils {

    private static String userId=null;

    public static String getCurrentUserId(DatabaseReference databaseReference) {
        if (userId==null){
            userId = databaseReference.push().getKey();
        }
        return userId;
    }

    public static long convertMegaBytesToBytes(long mb) {
        return mb * 1024 * 1024;

    }

    public static String getDateTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000");
        Date date = new Date();
        return simpleDateFormat.format(date);

    }


    public static int getColorCode(Context context, int position){
        int[] rainbow = context.getResources().getIntArray(R.array.randomColor);
        if (position<50){
           return rainbow[position];
        }else {
            return rainbow[position%50];
        }
    }

    public static void moveToSignInActivity(final Context context) {

        /*new AlertDialog.Builder(context)
                .setMessage("You need to login first!!!")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context, SignInActivity.class);
                        i.putExtra(Constant.OTHER_THAN_SPLASH_SCREEN, true);
                        context.startActivity(i);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/

        ViewDialog.showDialog((Activity) context, Constant.SignInMessage, Constant.SignInTitle);


    }

    public static File completeInternalStoragePath(Context context, int type) {
        return new File(context.getFilesDir(), subPath(type));

    }

    public static File completePathInSDCard(int type) {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(), subPath(type));
    }


    public static String subPath(int type) {
        String subPath = "";
        switch (type) {
            case Constant.IMAGE:
                subPath = Constant.RINGY_IMAGE;
                break;
            case Constant.PDF:
                subPath = Constant.RINGY_PDF;
                break;
            case Constant.VIDEO:
                subPath = Constant.RINGY_VIDEO;
                break;
            case Constant.AUDIO:
                subPath = Constant.RINGY_AUDIO;
                break;
            case Constant.AUDIO_HIDDEN:
                subPath = Constant.RINGY_AUDIO_HIDDEN;
                break;
        }
        return subPath;
    }

    public static long getExternalFreeSpace() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize()) / 1048576;

    }
}
