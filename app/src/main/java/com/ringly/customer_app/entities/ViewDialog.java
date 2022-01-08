package com.ringly.customer_app.entities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ringly.customer_app.R;
import com.ringly.customer_app.views.activities.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ViewDialog {

    public ViewDialog() {
    }

    public static void showDialog(final Activity activity, String msg, String title){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvDialogMessage = dialog.findViewById(R.id.tvDialogMessage);
        tvTitle.setText(title);
        tvDialogMessage.setText(msg);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                new MySharedPref(activity).deleteAllData();
                Intent i = new Intent(activity, SignInActivity.class);
                i.putExtra(Constant.OTHER_THAN_SPLASH_SCREEN, true);
                activity.startActivity(i);
                activity.finish();
            }
        });

        dialog.show();

    }
}
