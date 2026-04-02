package com.adssdkmanager.ads_manager;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.common.OnAdCallBack;
import com.adssdkmanager.loadData.AdsPreferences;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

public class AppOpenManager {

    private AppOpenAd appOpenAd = null;
    Activity activity;

    public AppOpenManager(Activity activity) {
        this.activity = activity;
    }

    public void Google_Admob_Appopen(OnAdCallBack onAdCallBack) {
        if (CommonData.is_ShowingAd) {
            return;
        }

        CommonData.LogStartAppopen();
        String logMsg = CommonData.ads_Priority + " -> Google_Admob_Appopen() -> ";
        CommonData.LogAppopen(logMsg + "Start");

        AdRequest request = new AdRequest.Builder().build();

        AppOpenAd.load(activity, new AdsPreferences(activity).getAdmobAppOpen(), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                CommonData.LogAppopen(logMsg + "onAdLoaded");
                CommonData.is_ShowingAd = true;
                appOpenAd = ad;
                appOpenAd.show(activity);

                appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        CommonData.LogAppopen(logMsg + "onAdDismissedFullScreenContent");
                        appOpenAd = null;
                        CommonData.is_ShowingAd = false;

                        onAdCallBack.onAdDismiss();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        CommonData.LogAppopen(logMsg + "onAdFailedToShowFullScreenContent");
                        appOpenAd = null;
                        CommonData.is_ShowingAd = false;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                CommonData.LogAppopen(logMsg + "onAdFailedToLoad");

                if (CommonData.checkAppopenAdUnit(activity)) {
                    new OtherAdsManager(activity).showQurekaAppopen(onAdCallBack);
                } else {
                    Google_Adx_Appopen(onAdCallBack);
                }
            }
        });

    }

    public void Google_Adx_Appopen(OnAdCallBack onAdCallBack) {
        if (CommonData.is_ShowingAd) {
            return;
        }

        CommonData.LogStartAppopen();
        String logMsg = CommonData.ads_Priority + " -> Google_Adx_Appopen() -> ";
        CommonData.LogAppopen(logMsg + "Start");

        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(activity, new AdsPreferences(activity).getAdxAppOpen(), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                CommonData.LogAppopen(logMsg + "onAdLoaded");
                CommonData.is_ShowingAd = true;
                appOpenAd = ad;
                appOpenAd.show(activity);

                appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        CommonData.LogAppopen(logMsg + "onAdDismissedFullScreenContent");
                        appOpenAd = null;
                        CommonData.is_ShowingAd = false;

                        onAdCallBack.onAdDismiss();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        CommonData.LogAppopen(logMsg + "onAdFailedToShowFullScreenContent");
                        appOpenAd = null;
                        CommonData.is_ShowingAd = false;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        CommonData.LogAppopen(logMsg + "onAdShowedFullScreenContent");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                CommonData.LogAppopen(logMsg + "onAdFailedToLoad");
                appOpenAd = null;
                CommonData.is_ShowingAd = false;

                new OtherAdsManager(activity).showQurekaAppopen(onAdCallBack);

            }
        });

    }

    public void showAppopenAdResume() {
        if (CommonData.checkAppUpdate(activity)) {
            return;
        }
        if (CommonData.is_ShowingAd) {
            return;
        }
        if (new AdsPreferences(activity).getAdsOnFlag()) {
            if (CommonData.isAppopenAvailableToShow) {
                Google_Admob_Appopen_OnResume();
            }
        }
    }

    public void Google_Admob_Appopen_OnResume() {
        CommonData.LogStartAppopen();
        String logMsg = CommonData.ads_Priority + " -> Google_Admob_Appopen_OnResume() -> ";
        CommonData.LogAppopen(logMsg + "Start");

        AdRequest request = new AdRequest.Builder().build();

        AppOpenAd.load(activity, new AdsPreferences(activity).getAdmobAppOpen(), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                CommonData.is_ShowingAd = true;
                CommonData.LogAppopen(logMsg + "onAdLoaded");
                CommonData._appOpenAd_google = ad;

                CommonData._appOpenAd_google.show(activity);

                CommonData._appOpenAd_google.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        CommonData.is_ShowingAd = false;
                        CommonData.LogAppopen(logMsg + "onAdDismissedFullScreenContent");
                        CommonData._appOpenAd_google = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        CommonData.is_ShowingAd = false;
                        CommonData.LogAppopen(logMsg + "onAdFailedToShowFullScreenContent");
                        CommonData._appOpenAd_google = null;
                        Google_Adx_Appopen_OnResume();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        CommonData.LogAppopen(logMsg + "onAdShowedFullScreenContent");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                CommonData.LogAppopen(logMsg + "onAdFailedToLoad");
                CommonData._appOpenAd_google = null;
                CommonData.is_ShowingAd = false;

                if (CommonData.checkAppopenAdUnit(activity)) {
                    new OtherAdsManager(activity).showQurekaAppopen(new OnAdCallBack() {
                        @Override
                        public void onAdDismiss() {

                        }
                    });
                } else {
                    Google_Adx_Appopen_OnResume();
                }
            }
        });
    }

    public void Google_Adx_Appopen_OnResume() {
        CommonData.LogStartAppopen();
        String logMsg = CommonData.ads_Priority + " -> Google_Adx_Appopen_OnResume() -> ";
        CommonData.LogAppopen(logMsg + "Start");

        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(activity, new AdsPreferences(activity).getAdxAppOpen(), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                CommonData.is_ShowingAd = true;
                CommonData.LogAppopen(logMsg + "onAdLoaded");
                CommonData._appOpenAd_google = ad;

                CommonData._appOpenAd_google.show(activity);

                CommonData._appOpenAd_google.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        CommonData.is_ShowingAd = false;
                        CommonData.LogAppopen(logMsg + "onAdDismissedFullScreenContent");
                        CommonData._appOpenAd_google = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        CommonData.is_ShowingAd = false;
                        CommonData.LogAppopen(logMsg + "onAdFailedToShowFullScreenContent");
                        CommonData._appOpenAd_google = null;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        CommonData.LogAppopen(logMsg + "onAdShowedFullScreenContent");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                CommonData.LogAppopen(logMsg + "onAdFailedToLoad");
                CommonData._appOpenAd_google = null;
                CommonData.is_ShowingAd = false;

                new OtherAdsManager(activity).showQurekaAppopen(new OnAdCallBack() {
                    @Override
                    public void onAdDismiss() {

                    }
                });
            }
        });
    }

}
