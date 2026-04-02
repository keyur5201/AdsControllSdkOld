package com.adsv2.ads.control.sdk.activity;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adssdkmanager.ads_manager.AdsManager;
import com.adssdkmanager.common.OnAdCallBack;
import com.adssdkmanager.common.RateUsDialog;
import com.adssdkmanager.loadData.GetDataFromServer;
import com.adsv2.ads.control.sdk.R;
import com.bumptech.glide.Glide;


public class OneActivity extends AppCompatActivity {

    TextView oneB;
    TextView twoB;
    TextView threeB;

    public AppOpsManager.OnOpChangedListener Y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        new GetDataFromServer(this).initHydraSdk(new OnAdCallBack() {
            @Override
            public void onAdDismiss() {

            }
        });

//        checkUsageAccess();

        ImageView image = findViewById(R.id.image);

        Glide.with(this).load("https://play-lh.googleusercontent.com/ccWDU4A7fX1R24v-vvT480ySh26AYp97g1VrIB_FIdjRcuQB2JP2WdY7h_wVVAeSpg=w240-h480-rw").into(image);

        SharedPreferences sharedPreferences = getSharedPreferences("AdSize", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("adsSize", 1);
        myEdit.apply();

        oneB = findViewById(R.id.oneB);
        twoB = findViewById(R.id.twoB);
        threeB = findViewById(R.id.threeB);

//        String encryptedString = Encode.encode("");
//        String decryptedString = Decode.decode("");

        oneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneB.setBackgroundResource(R.drawable.selected_bg);
                twoB.setBackgroundResource(R.drawable.unselected_bg);
                threeB.setBackgroundResource(R.drawable.unselected_bg);
                myEdit.putInt("adsSize", 1);
                myEdit.apply();
            }
        });

        twoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneB.setBackgroundResource(R.drawable.unselected_bg);
                twoB.setBackgroundResource(R.drawable.selected_bg);
                threeB.setBackgroundResource(R.drawable.unselected_bg);
                myEdit.putInt("adsSize", 2);
                myEdit.apply();
            }
        });

        threeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneB.setBackgroundResource(R.drawable.unselected_bg);
                twoB.setBackgroundResource(R.drawable.unselected_bg);
                threeB.setBackgroundResource(R.drawable.selected_bg);
                myEdit.putInt("adsSize", 3);
                myEdit.apply();
            }
        });

        if (!isAccessGranted()) {
//            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//            startActivity(intent);

            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);

            appOpsManager.startWatchingMode("android:system_alert_window", null, Y);
        }

    }

    public class EM1 implements AppOpsManager.OnOpChangedListener {
        public EM1() {
        }

        @Override // android.app.AppOpsManager.OnOpChangedListener
        public void onOpChanged(String str, String str2) {
            if (getPackageName().equals(str2) && "android:system_alert_window".equals(str)) {
                F();
            }
        }
    }

    public void F() {
        AppOpsManager.OnOpChangedListener onOpChangedListener;
        AppOpsManager appOpsManager = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
        if (appOpsManager == null || (onOpChangedListener = Y) == null) {
            return;
        }
        appOpsManager.stopWatchingMode(onOpChangedListener);
        Y = null;
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean checkUsageAccess(){
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void showNativeBanner(View view) {
        new AdsManager(this).startActivity(SecondActivity.class);
    }

    public void showNativeSmall(View view) {
        new AdsManager(this).startActivity(ThreeActivity.class);
    }

    public void showNativeRecycler(View view) {
//        new AdsManager(this).startActivity(RecyclerActivity.class);
        new AdsManager(this).startActivity(RecyclerActivity.class);
    }

    public void showNativeGrid(View view) {
        new AdsManager(this).startActivity(GridRecyclerActivity.class);
    }

    @Override
    public void onBackPressed() {
        new RateUsDialog(this).ShowRateUsDialog(R.mipmap.ic_launcher, R.string.app_name);
    }

}