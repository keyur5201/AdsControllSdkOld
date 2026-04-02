package com.adssdkmanager.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.adssdkmanager.loadData.AdsPreferences;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class CommonData {

    public static final String TAG = "AdsLogy";
    public static boolean is_ShowingAd;

    // Google
    public static com.google.android.gms.ads.AdView google_BannerAds;
    public static com.google.android.gms.ads.nativead.NativeAd google_NativeAds;
    public static com.google.android.gms.ads.nativead.NativeAd google_NativeBannerAds;
    public static com.google.android.gms.ads.nativead.NativeAd google_NativeSmallAds;
    public static com.google.android.gms.ads.interstitial.InterstitialAd _interstitial_google;
    public static com.google.android.gms.ads.interstitial.InterstitialAd _interstitial_google_compulsory;
    public static AppOpenAd _appOpenAd_google;

    // Facebook
    public static com.facebook.ads.AdView facebook_bannerAd;
    public static NativeAd facebook_nativeAd_1, facebook_nativeAd_2;
    public static NativeAd facebook_nativeBannerAd_1, facebook_nativeBannerAd_2;
    public static com.facebook.ads.NativeBannerAd facebook_nativeSmallAd_1, facebook_nativeSmallAd_2;
    public static com.facebook.ads.InterstitialAd _interstitial_facebook;
    public static com.facebook.ads.InterstitialAd _interstitial_facebook_compulsory;


    //Ads Priiority
    public static boolean is_Google;
    public static boolean is_Google_Facebook;
    public static boolean is_Facebook;
    public static boolean is_Facebook_Google;
    public static boolean is_Alternate;


    public static boolean is_Facebook_Rv;
    public static boolean is_Google_Rv;

    public static NativeAdsManager nativeAdsManager;
    public static List<NativeAd> nativeAdList = new ArrayList<>();

    public static String ads_Priority;

    public static String other_bottom_banner_folder = "bottom_banner,7,5";
    public static String other_fullscreen_image_folder = "fullscreen_image,7,22";
    public static String other_gif_folder = "gif,13,9";
    public static String other_icon_round_folder = "icon_round,10,8";
    public static String other_icon_square_folder = "icon_square,14,32";
    public static String other_media_image_folder = "media_image,70,134";

    public static int ItemsPerAdsNormal;
    public static int GridCount = 3;

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_AD = 1;
    public static final int Fb_Rv_FirstAd_Position = 0;

    public static boolean isAppopenAvailableToShow = false;


//================ OneSignal Sdk Initialization ====================//
//================ OneSignal Sdk Initialization ====================//

    public static void initOneSignalSdk(Context context, String _id) {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(context);
        OneSignal.setAppId(_id);
        OneSignal.promptForPushNotifications();
    }

//================ OneSignal Sdk Initialization ====================//
//================ OneSignal Sdk Initialization ====================//

    public static void LogAppopen(String msg) {
        Log.i(CommonData.TAG, "AdsManagerAppopen -> " + msg);
    }

    public static void LogNative(String msg) {
        Log.i(CommonData.TAG, "AdsManagerNative -> " + msg);
    }

    public static void LogInterstitial(String msg) {
        Log.i(CommonData.TAG, "AdsManagerInterstitial -> " + msg);
    }

    public static void LogBanner(String msg) {
        Log.i(CommonData.TAG, "AdsManagerBanner -> " + msg);
    }

    public static void LogDataLoad(String msg) {
        Log.i(CommonData.TAG, "AdsManagerDataLoad -> " + msg);
    }

    public static void LogStartAppopen() {
        Log.i(CommonData.TAG, "AdsManagerAppopen -> <======================*****************======================>");
    }

    public static void LogStartNative() {
        Log.i(CommonData.TAG, "AdsManagerNative -> <======================*****************======================>");
    }

    public static void LogStartInterstitial() {
        Log.i(CommonData.TAG, "AdsManagerInterstitial -> <======================*****************======================>");
    }

    public static void LogStartBanner() {
        Log.i(CommonData.TAG, "AdsManagerBanner -> <======================*****************======================>");
    }

    public static void LogStartDataLoad() {
        Log.i(CommonData.TAG, "AdsManagerDataLoad -> <======================*****************======================>");
    }

    public static void setPriorityClickData(Activity activity) {
        CommonData.ItemsPerAdsNormal = new AdsPreferences(activity).getAdsRecyclerPosition();
        CheckAdsPriority(activity);
        CheckRvAdsPriority();
        setAdsClick(activity);
    }

    public static void CheckAdsPriority(Activity activity) {
        String priority = new AdsPreferences(activity).getAdsPriority();
        CommonData.is_Google = false;
        CommonData.is_Google_Facebook = false;
        CommonData.is_Facebook = false;
        CommonData.is_Facebook_Google = false;
        CommonData.is_Alternate = false;
        if (priority.equalsIgnoreCase("g")) {
            CommonData.is_Google = true;
            CommonData.ads_Priority = "Google";
        } else if (priority.equalsIgnoreCase("gf")) {
            CommonData.is_Google_Facebook = true;
            CommonData.ads_Priority = "Google_Facebook";
        } else if (priority.equalsIgnoreCase("f")) {
            CommonData.is_Facebook = true;
            CommonData.ads_Priority = "Facebook";
        } else if (priority.equalsIgnoreCase("fg")) {
            CommonData.is_Facebook_Google = true;
            CommonData.ads_Priority = "Facebook_Google";
        } else if (priority.equalsIgnoreCase("alt")) {
            CommonData.is_Alternate = true;
            CommonData.ads_Priority = "Alternate";
        } else if (priority.equalsIgnoreCase("Google")) {
            CommonData.is_Google = true;
            CommonData.ads_Priority = "Google";
        } else if (priority.equalsIgnoreCase("Google_Facebook")) {
            CommonData.is_Google_Facebook = true;
            CommonData.ads_Priority = "Google_Facebook";
        } else if (priority.equalsIgnoreCase("Facebook")) {
            CommonData.is_Facebook = true;
            CommonData.ads_Priority = "Facebook";
        } else if (priority.equalsIgnoreCase("Facebook_Google")) {
            CommonData.is_Facebook_Google = true;
            CommonData.ads_Priority = "Facebook_Google";
        } else if (priority.equalsIgnoreCase("Alternate")) {
            CommonData.is_Alternate = true;
            CommonData.ads_Priority = "Alternate";
        } else {
            CommonData.is_Google_Facebook = true;
            CommonData.ads_Priority = "Google_Facebook";
        }
        CheckRvAdsPriority();
    }

    public static void CheckRvAdsPriority() {
        CommonData.is_Google_Rv = false;
        CommonData.is_Facebook_Rv = false;
        if (CommonData.is_Facebook || CommonData.is_Facebook_Google) {
            CommonData.is_Facebook_Rv = true;
        } else {
            CommonData.is_Google_Rv = true;
        }
    }

    public static boolean checkInterstitalAdUnit(Activity activity) {
        return new AdsPreferences(activity).getAdmobInterstitial().equalsIgnoreCase(new AdsPreferences(activity).getAdxInterstitial());
    }

    public static boolean checkAppopenAdUnit(Activity activity) {
        return new AdsPreferences(activity).getAdmobAppOpen().equalsIgnoreCase(new AdsPreferences(activity).getAdxAppOpen());
    }

    public static boolean checkNativeAdUnit(Activity activity) {
        return new AdsPreferences(activity).getAdmobNative().equalsIgnoreCase(new AdsPreferences(activity).getAdxNative());
    }

    public static boolean checkNativebannerAdUnit(Activity activity) {
        return new AdsPreferences(activity).getAdmobNativeBanner().equalsIgnoreCase(new AdsPreferences(activity).getAdxNativeBanner());
    }

    public static boolean checkBannerAdUnit(Activity activity) {
        return new AdsPreferences(activity).getAdmobBanner().equalsIgnoreCase(new AdsPreferences(activity).getAdxBanner());
    }

    public static boolean isAdsGoogleClickMatch(Activity activity) {
        return new AdsPreferences(activity).getGoogleAdsClickCount() >= new AdsPreferences(activity).getAdsClickGoogle();
    }

    public static boolean isAdsFbClickMatch(Activity activity) {
        return new AdsPreferences(activity).getFbAdsClickCount() >= new AdsPreferences(activity).getAdsClickFacebook();
    }

    public static boolean isInterPreload(Activity activity) {
        return new AdsPreferences(activity).getInterstitialPreload();
    }

    public static boolean isNativePreload(Activity activity) {
        return new AdsPreferences(activity).getNativePreload();
    }

    public static boolean checkAppUpdate(Activity activity) {
        return new AdsPreferences(activity).getAppUpdateFlag() && new AdsPreferences(activity).getAppCurrentVersion()
                .equalsIgnoreCase(new AdsPreferences(activity).getDefaultAppVersion());
    }

    public static String checkAdsCompulsoryPriority(int attrSelect) {
        if (attrSelect == 1) {
            return "off";
        } else if (attrSelect == 2) {
            return "google";
        } else if (attrSelect == 3) {
            return "facebook";
        } else {
            return "off";
        }
    }

    public static void setAdsClick(Activity activity) {
        new AdsPreferences(activity).setGoogleAdsClickCount(100);
        new AdsPreferences(activity).setFbAdsClickCount(100);
    }

    public static boolean isShowOtherAds(Activity activity) {
        return !new AdsPreferences(activity).getOtherAds().equalsIgnoreCase("off");
    }

    public static boolean isAdsButtonColorFlag(Activity activity) {
        return !new AdsPreferences(activity).getAdsButtonColor().equalsIgnoreCase("off");
    }
}
