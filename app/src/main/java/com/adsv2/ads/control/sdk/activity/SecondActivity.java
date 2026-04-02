package com.adsv2.ads.control.sdk.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.adsv2.ads.control.sdk.R;
import com.adssdkmanager.ads_manager.AdsManager;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    public void onBackPressed() {
        new AdsManager(this).onBackPress();
    }

    public void showNativeBannerMid(View view) {
        new AdsManager(this).startActivity(ThreeActivity.class);
    }
}