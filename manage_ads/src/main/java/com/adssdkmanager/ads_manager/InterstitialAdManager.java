package com.adssdkmanager.ads_manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.adssdkmanager.R;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.common.OnAdCallBack;
import com.adssdkmanager.loadData.AdsPreferences;
import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InterstitialAdManager {

    Activity activity;
    private Dialog loading;

    public InterstitialAdManager(Activity activity) {
        this.activity = activity;
    }

    public void ShowInterstitialAd(OnAdCallBack onAdCallBack) {
        if (CommonData.checkAppUpdate(activity)) {
            onAdCallBack.onAdDismiss();
        } else {
            if (new AdsPreferences(activity).getAdsOnFlag()) {
                show_InterstitialAds(onAdCallBack);
            } else {
                onAdCallBack.onAdDismiss();
            }
        }
    }

    public void show_InterstitialAds(OnAdCallBack onAdCallBack) {
        if (CommonData.isInterPreload(activity)) {
            if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                if (CommonData.isAdsGoogleClickMatch(activity)) {
                    google_Interstitial_show(onAdCallBack);
                } else {
                    if (new AdsPreferences(activity).getShowGfInterstitial()) {
                        if (CommonData.isAdsFbClickMatch(activity)) {
                            fb_Interstitial_Compulsory(onAdCallBack);
                        } else {
                            onAdCallBack.onAdDismiss();
                        }
                    } else {
                        onAdCallBack.onAdDismiss();
                    }
                }
            } else {
                if (CommonData.isAdsFbClickMatch(activity)) {
                    fb_Interstitial_show(onAdCallBack);
                } else {
                    if (new AdsPreferences(activity).getShowGfInterstitial()) {
                        if (CommonData.isAdsGoogleClickMatch(activity)) {
                            admob_Interstitial_Compulsory(onAdCallBack);
                        } else {
                            onAdCallBack.onAdDismiss();
                        }
                    } else {
                        onAdCallBack.onAdDismiss();
                    }
                }
            }
        } else {
            if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                if (CommonData.isAdsGoogleClickMatch(activity)) {
                    admob_Interstitial_load(onAdCallBack);
                } else {
                    if (new AdsPreferences(activity).getShowGfInterstitial()) {
                        if (CommonData.isAdsFbClickMatch(activity)) {
                            fb_Interstitial_Compulsory(onAdCallBack);
                        } else {
                            onAdCallBack.onAdDismiss();
                        }
                    } else {
                        onAdCallBack.onAdDismiss();
                    }
                }
            } else {
                if (CommonData.isAdsFbClickMatch(activity)) {
                    fb_Interstitial_load(onAdCallBack);
                } else {
                    if (new AdsPreferences(activity).getShowGfInterstitial()) {
                        if (CommonData.isAdsGoogleClickMatch(activity)) {
                            Log.i("dfhrfyjtfgj", "show_InterstitialAds: " + new AdsPreferences(activity).getGoogleAdsClickCount());
                            admob_Interstitial_Compulsory(onAdCallBack);
                        } else {
                            onAdCallBack.onAdDismiss();
                        }
                    } else {
                        onAdCallBack.onAdDismiss();
                    }
                }
            }
        }

        new AdsPreferences(activity).setGoogleAdsClickCount(new AdsPreferences(activity).getGoogleAdsClickCount() + 1);
        new AdsPreferences(activity).setFbAdsClickCount(new AdsPreferences(activity).getFbAdsClickCount() + 1);
    }

    public void google_Interstitial_show(OnAdCallBack onAdCallBack) {
        String logMsg = CommonData.ads_Priority + " -> google_Interstitial_show() -> ";

        if (CommonData._interstitial_google != null) {
            CommonData.is_ShowingAd = true;
            CommonData._interstitial_google.show(activity);

            CommonData._interstitial_google.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    CommonData.LogInterstitial(logMsg + "onAdDismissedFullScreenContent");
                    CommonData._interstitial_google = null;
                    CommonData.is_ShowingAd = false;
                    new AdsPreferences(activity).setGoogleAdsClickCount(0);

                    if (CommonData.isInterPreload(activity)) {
                        admob_Interstitial_load(onAdCallBack);
                    }

                    if (new AdsPreferences(activity).getShowGfInterstitial() && CommonData.isAdsFbClickMatch(activity)) {
                        fb_Interstitial_Compulsory(onAdCallBack);
                    } else {
                        onAdCallBack.onAdDismiss();
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    CommonData.LogInterstitial(logMsg + "onAdFailedToShowFullScreenContent");
                    CommonData._interstitial_google = null;
                    dismiss_Loading();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    dismiss_Loading();
                    CommonData.LogInterstitial(logMsg + "onAdShowedFullScreenContent");
                }
            });
        } else {
            if (CommonData.is_Google_Facebook) {
                fb_Interstitial_show(onAdCallBack);
            } else {
                dismiss_Loading();
                new OtherAdsManager(activity).showQurekaInterstitial(onAdCallBack);
            }
        }
    }

    public void fb_Interstitial_show(OnAdCallBack onAdCallBack) {
        String logMsg = CommonData.ads_Priority + " -> fb_Interstitial_show() -> ";

        if (CommonData._interstitial_facebook != null) {
            CommonData.is_ShowingAd = true;
            CommonData._interstitial_facebook.show();
            CommonData._interstitial_facebook.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    dismiss_Loading();
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    CommonData.is_ShowingAd = false;
                    CommonData._interstitial_facebook = null;
                    new AdsPreferences(activity).setFbAdsClickCount(0);
                    CommonData.LogInterstitial(logMsg + "onInterstitialDismissed");

                    if (CommonData.isInterPreload(activity)) {
                        fb_Interstitial_load(onAdCallBack);
                    }

                    if (new AdsPreferences(activity).getShowGfInterstitial() && CommonData.isAdsGoogleClickMatch(activity)) {
                        admob_Interstitial_Compulsory(onAdCallBack);
                    } else {
                        onAdCallBack.onAdDismiss();
                    }

                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    CommonData.is_ShowingAd = false;
                    CommonData._interstitial_facebook = null;
                }

                @Override
                public void onAdLoaded(Ad ad) {

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });
        } else {
            if (CommonData.is_Facebook_Google) {
                google_Interstitial_show(onAdCallBack);
            } else {
                dismiss_Loading();
                new OtherAdsManager(activity).showQurekaInterstitial(onAdCallBack);
            }
        }
    }

    public void admob_Interstitial_load(OnAdCallBack onAdCallBack) {
        try {
            if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                show_Loading();
            }
            CommonData.LogStartInterstitial();
            String logMsg = CommonData.ads_Priority + " -> admob_Interstitial_load() -> ";
            CommonData.LogInterstitial(logMsg + "Start");

            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, new AdsPreferences(activity).getAdmobInterstitial(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    CommonData.LogInterstitial(logMsg + "onAdLoaded");
                    CommonData._interstitial_google = interstitialAd;

                    if (!CommonData.isInterPreload(activity)) {
                        google_Interstitial_show(onAdCallBack);
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    CommonData.LogInterstitial(logMsg + "onAdFailedToLoad");
                    CommonData._interstitial_google = null;

                    if (CommonData.checkInterstitalAdUnit(activity)) {
                        if (CommonData.is_Google_Facebook) {
                            new AdsPreferences(activity).setGoogleAdsClickCount(0);
                            fb_Interstitial_load(onAdCallBack);
                        } else {
                            dismiss_Loading();
                            if (!CommonData.isInterPreload(activity)) {
                                new OtherAdsManager(activity).showQurekaInterstitial(onAdCallBack);
                            }
                        }
                    } else {
                        adx_Interstitial_load(onAdCallBack);
                    }

                }
            });
        } catch (WindowManager.BadTokenException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void adx_Interstitial_load(OnAdCallBack onAdCallBack) {
        try {
            CommonData.LogStartInterstitial();
            String logMsg = CommonData.ads_Priority + " -> adx_Interstitial_load() -> ";
            CommonData.LogInterstitial(logMsg + "Start");

            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, new AdsPreferences(activity).getAdxInterstitial(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    CommonData.LogInterstitial(logMsg + "onAdLoaded");
                    CommonData._interstitial_google = interstitialAd;

                    if (!CommonData.isInterPreload(activity)) {
                        google_Interstitial_show(onAdCallBack);
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    CommonData.LogInterstitial(logMsg + "onAdFailedToLoad");
                    CommonData._interstitial_google = null;

                    if (CommonData.is_Google_Facebook) {
                        new AdsPreferences(activity).setGoogleAdsClickCount(0);
                        fb_Interstitial_load(onAdCallBack);
                    } else {
                        dismiss_Loading();
                        if (!CommonData.isInterPreload(activity)) {
                            new OtherAdsManager(activity).showQurekaInterstitial(onAdCallBack);
                        }
                    }
                }
            });
        } catch (WindowManager.BadTokenException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void fb_Interstitial_load(OnAdCallBack onAdCallBack) {
        try {
            if (CommonData.is_Facebook || CommonData.is_Facebook_Google) {
                show_Loading();
            }

            CommonData.LogStartInterstitial();
            String logMsg = CommonData.ads_Priority + " -> fb_Interstitial_load() -> ";
            CommonData.LogInterstitial(logMsg + "Start");

            CommonData._interstitial_facebook = new com.facebook.ads.InterstitialAd(activity, new AdsPreferences(activity).getFacebookInterstitial());

            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onInterstitialDisplayed");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    CommonData.is_ShowingAd = false;
                    new AdsPreferences(activity).setFbAdsClickCount(0);
                    CommonData.LogInterstitial(logMsg + "onInterstitialDismissed");
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    CommonData._interstitial_facebook = null;
                    CommonData.LogInterstitial(logMsg + "onError");

                    fb_Interstitial_load2(onAdCallBack);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onAdLoaded");
                    if (!CommonData.isInterPreload(activity)) {
                        fb_Interstitial_show(onAdCallBack);
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onAdClicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onLoggingImpression");
                }
            };

            CommonData._interstitial_facebook.loadAd(CommonData._interstitial_facebook.buildLoadAdConfig().withAdListener(interstitialAdListener).build());

        } catch (WindowManager.BadTokenException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void fb_Interstitial_load2(OnAdCallBack onAdCallBack) {
        try {
            CommonData.LogStartInterstitial();
            String logMsg = CommonData.ads_Priority + " -> fb_Interstitial_load2() -> ";
            CommonData.LogInterstitial(logMsg + "Start");

            CommonData._interstitial_facebook = new com.facebook.ads.InterstitialAd(activity, new AdsPreferences(activity).getFacebookInterstitial());

            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onInterstitialDisplayed");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    CommonData.is_ShowingAd = false;
                    new AdsPreferences(activity).setFbAdsClickCount(0);
                    CommonData.LogInterstitial(logMsg + "onInterstitialDismissed");
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    CommonData._interstitial_facebook = null;
                    CommonData.LogInterstitial(logMsg + "onError");

                    if (CommonData.is_Facebook_Google) {
                        new AdsPreferences(activity).setFbAdsClickCount(0);
                        admob_Interstitial_load(onAdCallBack);
                    } else {
                        dismiss_Loading();
                        if (!CommonData.isInterPreload(activity)) {
                            new OtherAdsManager(activity).showQurekaInterstitial(onAdCallBack);
                        }
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onAdLoaded");
                    if (!CommonData.isInterPreload(activity)) {
                        fb_Interstitial_show(onAdCallBack);
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onAdClicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onLoggingImpression");
                }
            };

            CommonData._interstitial_facebook.loadAd(CommonData._interstitial_facebook.buildLoadAdConfig().withAdListener(interstitialAdListener).build());

        } catch (WindowManager.BadTokenException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void admob_Interstitial_Compulsory(OnAdCallBack onAdCallBack) {
        try {
            if (loading != null) {
                loading.dismiss();
            }
            loading = new Dialog(activity);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setGravity(Gravity.CENTER);
            loading.setContentView(R.layout.ad_loading);
            loading.show();

            CommonData.LogStartInterstitial();
            String logMsg = CommonData.ads_Priority + " -> admob_Interstitial_Compulsory() -> ";
            CommonData.LogInterstitial(logMsg + "Start");

            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, new AdsPreferences(activity).getAdmobInterstitial(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    CommonData.LogInterstitial(logMsg + "onAdLoaded");
                    CommonData._interstitial_google_compulsory = interstitialAd;
                    CommonData.is_ShowingAd = true;
                    CommonData._interstitial_google_compulsory.show(activity);

                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            CommonData.LogInterstitial(logMsg + "onAdDismissedFullScreenContent");
                            CommonData._interstitial_google_compulsory = null;
                            CommonData.is_ShowingAd = false;

                            new AdsPreferences(activity).setGoogleAdsClickCount(0);

                            onAdCallBack.onAdDismiss();

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            CommonData.LogInterstitial(logMsg + "onAdFailedToShowFullScreenContent");
                            CommonData._interstitial_google_compulsory = null;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            loading.dismiss();
                            CommonData.LogInterstitial(logMsg + "onAdShowedFullScreenContent");
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    CommonData.LogInterstitial(logMsg + "onAdFailedToLoad");
                    CommonData._interstitial_google_compulsory = null;
                    if (CommonData.checkInterstitalAdUnit(activity)) {
                        loading.dismiss();
                        onAdCallBack.onAdDismiss();
                    } else {
                        adx_Interstitial_Compulsory(onAdCallBack);
                    }
                }
            });
        } catch (WindowManager.BadTokenException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void adx_Interstitial_Compulsory(OnAdCallBack onAdCallBack) {
        try {
            CommonData.LogStartInterstitial();
            String logMsg = CommonData.ads_Priority + " -> adx_Interstitial_Compulsory() -> ";
            CommonData.LogInterstitial(logMsg + "Start");

            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, new AdsPreferences(activity).getAdxInterstitial(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    CommonData.LogInterstitial(logMsg + "onAdLoaded");
                    CommonData._interstitial_google_compulsory = interstitialAd;
                    CommonData.is_ShowingAd = true;
                    CommonData._interstitial_google_compulsory.show(activity);

                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            CommonData.LogInterstitial(logMsg + "onAdDismissedFullScreenContent");
                            CommonData._interstitial_google_compulsory = null;
                            CommonData.is_ShowingAd = false;

                            new AdsPreferences(activity).setGoogleAdsClickCount(0);

                            onAdCallBack.onAdDismiss();

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            CommonData.LogInterstitial(logMsg + "onAdFailedToShowFullScreenContent");
                            CommonData._interstitial_google_compulsory = null;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            loading.dismiss();
                            CommonData.LogInterstitial(logMsg + "onAdShowedFullScreenContent");
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    CommonData.LogInterstitial(logMsg + "onAdFailedToLoad");
                    CommonData._interstitial_google_compulsory = null;
                    loading.dismiss();
                    onAdCallBack.onAdDismiss();
                }
            });
        } catch (WindowManager.BadTokenException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void fb_Interstitial_Compulsory(OnAdCallBack onAdCallBack) {
        try {
            if (loading != null) {
                loading.dismiss();
            }
            loading = new Dialog(activity);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setGravity(Gravity.CENTER);
            loading.setContentView(R.layout.ad_loading);
            loading.show();

            CommonData.LogStartInterstitial();
            String logMsg = CommonData.ads_Priority + " -> fb_Interstitial_Compulsory() -> ";
            CommonData.LogInterstitial(logMsg + "Start");

            CommonData._interstitial_facebook_compulsory = new com.facebook.ads.InterstitialAd(activity, new AdsPreferences(activity).getFacebookInterstitial());

            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    loading.dismiss();
                    CommonData.LogInterstitial(logMsg + "onInterstitialDisplayed");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onInterstitialDismissed");
                    CommonData.is_ShowingAd = false;
                    CommonData._interstitial_facebook_compulsory = null;

                    new AdsPreferences(activity).setFbAdsClickCount(0);

                    onAdCallBack.onAdDismiss();

                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    CommonData.LogInterstitial(logMsg + "onError");
                    CommonData._interstitial_facebook_compulsory = null;
                    fb_Interstitial_Compulsory_2(onAdCallBack);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onAdLoaded");
                    if (CommonData._interstitial_facebook_compulsory == null || !CommonData._interstitial_facebook_compulsory.isAdLoaded()) {
                        return;
                    }
                    if (CommonData._interstitial_facebook_compulsory.isAdInvalidated()) {
                        return;
                    }
                    CommonData.is_ShowingAd = true;
                    CommonData._interstitial_facebook_compulsory.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onAdClicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onLoggingImpression");
                }
            };

            CommonData._interstitial_facebook_compulsory.loadAd(CommonData._interstitial_facebook_compulsory.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        } catch (WindowManager.BadTokenException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void fb_Interstitial_Compulsory_2(OnAdCallBack onAdCallBack) {
        try {
            CommonData.LogStartInterstitial();
            String logMsg = CommonData.ads_Priority + " -> fb_Interstitial_Compulsory_2() -> ";
            CommonData.LogInterstitial(logMsg + "Start");

            CommonData._interstitial_facebook_compulsory = new com.facebook.ads.InterstitialAd(activity, new AdsPreferences(activity).getFacebookInterstitial());

            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    onAdCallBack.onAdDismiss();
                    CommonData.LogInterstitial(logMsg + "onInterstitialDisplayed");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onInterstitialDismissed");
                    CommonData.is_ShowingAd = false;
                    CommonData._interstitial_facebook_compulsory = null;

                    new AdsPreferences(activity).setFbAdsClickCount(0);

                    onAdCallBack.onAdDismiss();

                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    CommonData.LogInterstitial(logMsg + "onError");
                    CommonData._interstitial_facebook_compulsory = null;
                    loading.dismiss();
                    onAdCallBack.onAdDismiss();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onAdLoaded");
                    if (CommonData._interstitial_facebook_compulsory == null || !CommonData._interstitial_facebook_compulsory.isAdLoaded()) {
                        return;
                    }
                    if (CommonData._interstitial_facebook_compulsory.isAdInvalidated()) {
                        return;
                    }
                    CommonData.is_ShowingAd = true;
                    CommonData._interstitial_facebook_compulsory.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onAdClicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    CommonData.LogInterstitial(logMsg + "onLoggingImpression");
                }
            };

            CommonData._interstitial_facebook_compulsory.loadAd(CommonData._interstitial_facebook_compulsory.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        } catch (WindowManager.BadTokenException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void show_Loading() {
        if (!CommonData.isInterPreload(activity)) {
            if (loading != null) {
                loading.dismiss();
            }
            loading = new Dialog(activity);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setGravity(Gravity.CENTER);
            loading.setContentView(R.layout.ad_loading);
            loading.show();
        }
    }

    public void dismiss_Loading() {
        if (!CommonData.isInterPreload(activity)) {
            if (loading != null) {
                loading.dismiss();
            }
        }
    }

}


