package com.adsv2.ads.control.sdk.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adssdkmanager.loadData.GetDataFromServer;
import com.adsv2.ads.control.sdk.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new GetDataFromServer(this).loadAdsData(getPackageName(), OneActivity.class, OneActivity.class);

    }

}
