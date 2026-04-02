package com.adssdkmanager.ads_manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.adssdkmanager.BuildConfig;
import com.adssdkmanager.R;
import com.adssdkmanager.common.CommonAppMethodes;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.common.OnAdCallBack;
import com.adssdkmanager.loadData.AdsPreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AdsManager {

    Activity activity;

    public AdsManager(Activity activity) {
        this.activity = activity;
    }


    public void _nextActivity(OnAdCallBack onAdCallBack) {
        onAdCallBack.onAdDismiss();
    }

    public void startActivity(Intent intent) {
        new InterstitialAdManager(activity).ShowInterstitialAd(new OnAdCallBack() {
            @Override
            public void onAdDismiss() {
                activity.startActivity(intent);
            }
        });
    }

    public void startActivity(Class<?> className) {
        new InterstitialAdManager(activity).ShowInterstitialAd(new OnAdCallBack() {
            @Override
            public void onAdDismiss() {
                activity.startActivity(new Intent(activity, className));
            }
        });
    }

    public void startActivity(OnAdCallBack onAdCallBack) {
        new InterstitialAdManager(activity).ShowInterstitialAd(onAdCallBack);
    }

    public void startActivity(Intent intent, int requestCode) {
        new InterstitialAdManager(activity).ShowInterstitialAd(new OnAdCallBack() {
            @Override
            public void onAdDismiss() {
                activity.startActivityForResult(intent, requestCode);
            }
        });
    }

    public void startActivityCompulsory(Intent intent) {
        CommonData.setAdsClick(activity);
        new InterstitialAdManager(activity).ShowInterstitialAd(new OnAdCallBack() {
            @Override
            public void onAdDismiss() {
                activity.startActivity(intent);
            }
        });
    }

    public void startActivityCompulsory(Class<?> className) {
        CommonData.setAdsClick(activity);
        new InterstitialAdManager(activity).ShowInterstitialAd(new OnAdCallBack() {
            @Override
            public void onAdDismiss() {
                activity.startActivity(new Intent(activity, className));
            }
        });
    }

    public void startActivityCompulsory(OnAdCallBack onAdCallBack) {
        CommonData.setAdsClick(activity);
        new InterstitialAdManager(activity).ShowInterstitialAd(onAdCallBack);
    }

    public void startActivityCompulsory(Intent intent, int requestCode) {
        CommonData.setAdsClick(activity);
        new InterstitialAdManager(activity).ShowInterstitialAd(new OnAdCallBack() {
            @Override
            public void onAdDismiss() {
                activity.startActivityForResult(intent, requestCode);
            }
        });
    }

    public void onBackPress() {
        if (new AdsPreferences(activity).getAdsBackFlag()) {
            new InterstitialAdManager(activity).ShowInterstitialAd(new OnAdCallBack() {
                @Override
                public void onAdDismiss() {
                    activity.finish();
                }
            });
        } else {
            activity.finish();
        }
    }

    public void onBackPress(OnAdCallBack onAdCallBack) {
        if (new AdsPreferences(activity).getAdsBackFlag()) {
            new InterstitialAdManager(activity).ShowInterstitialAd(onAdCallBack);
        } else {
            onAdCallBack.onAdDismiss();
        }
    }

}
