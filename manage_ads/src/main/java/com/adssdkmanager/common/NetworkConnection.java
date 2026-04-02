package com.adssdkmanager.common;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.adssdkmanager.R;

public class NetworkConnection {

    Activity activity;

    public NetworkConnection(Activity activity) {
        this.activity = activity;
    }

    public boolean isNetworkConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnected();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void ShowNetworkDialog() {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.no_internet);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;
        window.setAttributes(attributes);

        dialog.show();

        TextView retry = dialog.findViewById(R.id.retry);

        retry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isNetworkConnected()) {
                    dialog.dismiss();
                }
            }
        });
    }

}
