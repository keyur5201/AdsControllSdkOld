package com.adssdkmanager.applevel;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.adssdkmanager.ads_manager.AppOpenManager;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.common.NetworkConnection;
import com.adssdkmanager.loadData.AdsPreferences;
import com.adssdkmanager.loadData.GetDataFromServer;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;



public class MyApplication extends Application implements LifecycleObserver, Application.ActivityLifecycleCallbacks, Configuration.Provider {
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .build();
    }

    Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        AudienceNetworkAds.initialize(this);


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(new AdsPreferences(getApplicationContext()).getOneSignalId());
        OneSignal.promptForPushNotifications();


        Configuration myConfig = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .build();
        WorkManager.initialize(this, myConfig);

        initHydraSdk();

    }

    public static void initHydraSdk() {
//        List<TransportConfig> transportConfigList = new ArrayList<>();
//        transportConfigList.add(HydraTransportConfig.create());
//        transportConfigList.add(OpenVpnTransportConfig.tcp());
//        transportConfigList.add(OpenVpnTransportConfig.udp());
//        UnifiedSdk.update(transportConfigList, CompletableCallback.EMPTY);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        new AppOpenManager(currentActivity).showAppopenAdResume();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        currentActivity = activity;
        if (!new NetworkConnection(activity).isNetworkConnected()) {
            new NetworkConnection(activity).ShowNetworkDialog();
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}