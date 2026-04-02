package com.adssdkmanager.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.adssdkmanager.R;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class RateUsDialog {

    Activity activity;

    public RateUsDialog(Activity activity) {
        this.activity = activity;
    }


    public void ShowRateUsDialog(int appIconImage, int appName) {

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.rate_us_dialog);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;
        window.setAttributes(attributes);

        dialog.show();

        TextView rateUsBtn = dialog.findViewById(R.id.rateUsBtn);
        TextView notNowBtn = dialog.findViewById(R.id.notNowBtn);
        TextView title = dialog.findViewById(R.id.title);
        ImageView appIcon = dialog.findViewById(R.id.appIcon);

        appIcon.setImageResource(appIconImage);

        title.setText("Enjoying " + activity.getString(appName) + "?");

        rateUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonAppMethodes(activity).RateApp();
            }
        });

        notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finishAffinity();
                System.exit(0);
            }
        });

    }

    public void InitUpdateManager() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(activity);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, activity, 1001);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Rate
    public void InitReviewManager() {
        ReviewManager manager = ReviewManagerFactory.create(activity);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Log.i(CommonData.TAG, "initReviewManager: " + "Review Successful");
                    } else {
                        Log.i(CommonData.TAG, "initReviewManager: " + "Error Review");
                    }
                });
            }
        });
    }

}
