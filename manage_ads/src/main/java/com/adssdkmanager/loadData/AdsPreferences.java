package com.adssdkmanager.loadData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AdsPreferences {

    public static final String USER_PREFS = "USER_PREFS";
    public SharedPreferences appSharedPref;
    public SharedPreferences.Editor prefEditor;

    public static final String fbAdsClickCount = "fbAdsClickCount";
    public static final String googleAdsClickCount = "googleAdsClickCount";
    public static final String defaultAppVersion = "defaultAppVersion";

    public static final String appUpdateFlag = "01_app_update_flag";
    public static final String appCurrentVersion = "02_app_current_version";
    public static final String adsOnFlag = "03_ads_on_flag";
    public static final String nativePreload = "04_native_preload";
    public static final String interstitialPreload = "05_interstitial_preload";
    public static final String adsPriority = "06_ads_priority";
    public static final String splashInterstitial = "07_splash_interstitial";
    public static final String showGfInterstitial = "08_show_gf_interstitial";
    public static final String adsClickGoogle = "09_ads_click_google";
    public static final String adsClickFacebook = "10_ads_click_facebook";
    public static final String showNativeLoader = "11_1_show_native_loader";
    public static final String adsBackFlag = "11_ads_back_flag";
    public static final String nativeShowBottom = "12_native_show_bottom";
    public static final String adsRecyclerPosition = "13_ads_recycler_position";
    public static final String otherAds = "14_other_ads";
    public static final String otherAdsUrl = "15_other_ads_url";
    public static final String adsButtonColor = "16_ads_button_color";
    public static final String privacyUrl = "17_privacy_url";
    public static final String oneSignalId = "18_one_signal_id";

    public static final String admobAppOpen = "21_admob_app_open";
    public static final String admobBanner = "22_admob_banner";
    public static final String admobInterstitial = "23_admob_interstitial";
    public static final String admobNative = "24_admob_native";
    public static final String admobNativeBanner = "25_admob_native_banner";
    public static final String adxAppOpen = "26_adx_app_open";
    public static final String adxBanner = "27_adx_banner";
    public static final String adxInterstitial = "28_adx_interstitial";
    public static final String adxNative = "29_adx_native";
    public static final String adxNativeBanner = "30_adx_native_banner";
    public static final String facebookBanner = "31_facebook_banner";
    public static final String facebookInterstitial = "32_facebook_interstitial";
    public static final String facebookNative = "33_facebook_native";
    public static final String facebookNativeBanner = "34_facebook_native_banner";

    public static final String serverFlag = "41_server_flag";
    public static final String serverBaseUrl = "42_server_base_url";
    public static final String serverCarrierid = "43_server_carrierid";
    public static final String serverCountry = "44_server_country";
    public static final String serverConnectCountryList = "45_server_connect_country_list";
    public static final String connectServerOnSplash = "46_connect_server_on_splash";


    public AdsPreferences(Context context) {
        appSharedPref = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
        prefEditor = appSharedPref.edit();
    }

    //=============== User Define - Start ===============//

    public String getDefaultAppVersion() {
        return appSharedPref.getString(defaultAppVersion, "1.0");
    }

    public void setDefaultAppVersion(String value) {
        prefEditor.putString(defaultAppVersion, value).apply();
    }

    public Integer getGoogleAdsClickCount() {
        return appSharedPref.getInt(googleAdsClickCount, 0);
    }

    public void setGoogleAdsClickCount(Integer value) {
        prefEditor.putInt(googleAdsClickCount, value).apply();
    }


    public Integer getFbAdsClickCount() {
        return appSharedPref.getInt(fbAdsClickCount, 0);
    }

    public void setFbAdsClickCount(Integer value) {
        prefEditor.putInt(fbAdsClickCount, value).apply();
    }

    //=============== User Define - End ===============//


    //=============== Ads Functionality - Start ===============//

    public boolean getAppUpdateFlag() {
        return appSharedPref.getBoolean(appUpdateFlag, true);
    }

    public void setAppUpdateFlag(boolean value) {
        prefEditor.putBoolean(appUpdateFlag, value).apply();
    }

    public String getAppCurrentVersion() {
        return appSharedPref.getString(appCurrentVersion, "1.0");
    }

    public void setAppCurrentVersion(String value) {
        prefEditor.putString(appCurrentVersion, value).apply();
    }

    public boolean getAdsOnFlag() {
        return appSharedPref.getBoolean(adsOnFlag, true);
    }

    public void setAdsOnFlag(boolean value) {
        prefEditor.putBoolean(adsOnFlag, value).apply();
    }

    public boolean getNativePreload() {
        return appSharedPref.getBoolean(nativePreload, true);
    }

    public void setNativePreload(boolean value) {
        prefEditor.putBoolean(nativePreload, value).apply();
    }

    public boolean getInterstitialPreload() {
        return appSharedPref.getBoolean(interstitialPreload, true);
    }

    public void setInterstitialPreload(boolean value) {
        prefEditor.putBoolean(interstitialPreload, value).apply();
    }

    public String getAdsPriority() {
        return appSharedPref.getString(adsPriority, "gf");
    }

    public void setAdsPriority(String value) {
        prefEditor.putString(adsPriority, value).apply();
    }

    public String getSplashInterstitial() {
        return appSharedPref.getString(splashInterstitial, "af");
    }

    public void setSplashInterstitial(String value) {
        prefEditor.putString(splashInterstitial, value).apply();
    }

    public boolean getShowGfInterstitial() {
        return appSharedPref.getBoolean(showGfInterstitial, false);
    }

    public void setShowGfInterstitial(boolean value) {
        prefEditor.putBoolean(showGfInterstitial, value).apply();
    }

    public Integer getAdsClickGoogle() {
        return appSharedPref.getInt(adsClickGoogle, 2);
    }

    public void setAdsClickGoogle(int value) {
        prefEditor.putInt(adsClickGoogle, value).apply();
    }

    public Integer getAdsClickFacebook() {
        return appSharedPref.getInt(adsClickFacebook, 2);
    }

    public void setAdsClickFacebook(int value) {
        prefEditor.putInt(adsClickFacebook, value).apply();
    }

    public boolean getShowNativeLoader() {
        return appSharedPref.getBoolean(showNativeLoader, false);
    }

    public void setShowNativeLoader(boolean value) {
        prefEditor.putBoolean(showNativeLoader, value).apply();
    }

    public boolean getAdsBackFlag() {
        return appSharedPref.getBoolean(adsBackFlag, true);
    }

    public void setAdsBackFlag(boolean value) {
        prefEditor.putBoolean(adsBackFlag, value).apply();
    }

    public String getNativeShowBottom() {
        return appSharedPref.getString(nativeShowBottom, "off");
    }

    public void setNativeShowBottom(String value) {
        prefEditor.putString(nativeShowBottom, value).apply();
    }

    public Integer getAdsRecyclerPosition() {
        return appSharedPref.getInt(adsRecyclerPosition, 7);
    }

    public void setAdsRecyclerPosition(Integer value) {
        prefEditor.putInt(adsRecyclerPosition, value).apply();
    }

    public String getOtherAds() {
        return appSharedPref.getString(otherAds, "qureka");
    }

    public void setOtherAds(String value) {
        prefEditor.putString(otherAds, value).apply();
    }

    public String getOtherAdsUrl() {
        return appSharedPref.getString(otherAdsUrl, "https://www.google.com");
    }

    public void setOtherAdsUrl(String value) {
        prefEditor.putString(otherAdsUrl, value).apply();
    }

    public String getAdsButtonColor() {
        return appSharedPref.getString(adsButtonColor, "#147DE3");
    }

    public void setAdsButtonColor(String value) {
        prefEditor.putString(adsButtonColor, value).apply();
    }

    public String getPrivacyUrl() {
        return appSharedPref.getString(privacyUrl, "");
    }

    public void setPrivacyUrl(String value) {
        prefEditor.putString(privacyUrl, value).apply();
    }

    public String getOneSignalId() {
        return appSharedPref.getString(oneSignalId, "");
    }

    public void setOneSignalId(String value) {
        prefEditor.putString(oneSignalId, value).apply();
    }

    //=============== Ads Functionality - End ===============//


    //=============== Ads Unit - Start ===============//

    public String getAdmobAppOpen() {
        return appSharedPref.getString(admobAppOpen, "");
    }

    public void setAdmobAppOpen(String value) {
        prefEditor.putString(admobAppOpen, value).apply();
    }

    public String getAdmobBanner() {
        return appSharedPref.getString(admobBanner, "");
    }

    public void setAdmobBanner(String value) {
        prefEditor.putString(admobBanner, value).apply();
    }

    public String getAdmobInterstitial() {
        return appSharedPref.getString(admobInterstitial, "");
    }

    public void setAdmobInterstitial(String value) {
        prefEditor.putString(admobInterstitial, value).apply();
    }

    public String getAdmobNative() {
        return appSharedPref.getString(admobNative, "");
    }

    public void setAdmobNative(String value) {
        prefEditor.putString(admobNative, value).apply();
    }

    public String getAdmobNativeBanner() {
        return appSharedPref.getString(admobNativeBanner, "");
    }

    public void setAdmobNativeBanner(String value) {
        prefEditor.putString(admobNativeBanner, value).apply();
    }

    public String getAdxAppOpen() {
        return appSharedPref.getString(adxAppOpen, "");
    }

    public void setAdxAppOpen(String value) {
        prefEditor.putString(adxAppOpen, value).apply();
    }

    public String getAdxBanner() {
        return appSharedPref.getString(adxBanner, "");
    }

    public void setAdxBanner(String value) {
        prefEditor.putString(adxBanner, value).apply();
    }

    public String getAdxInterstitial() {
        return appSharedPref.getString(adxInterstitial, "");
    }

    public void setAdxInterstitial(String value) {
        prefEditor.putString(adxInterstitial, value).apply();
    }

    public String getAdxNative() {
        return appSharedPref.getString(adxNative, "");
    }

    public void setAdxNative(String value) {
        prefEditor.putString(adxNative, value).apply();
    }

    public String getAdxNativeBanner() {
        return appSharedPref.getString(adxNativeBanner, "");
    }

    public void setAdxNativeBanner(String value) {
        prefEditor.putString(adxNativeBanner, value).apply();
    }

    public String getFacebookBanner() {
        return appSharedPref.getString(facebookBanner, "");
    }

    public void setFacebookBanner(String value) {
        prefEditor.putString(facebookBanner, value).apply();
    }

    public String getFacebookInterstitial() {
        return appSharedPref.getString(facebookInterstitial, "");
    }

    public void setFacebookInterstitial(String value) {
        prefEditor.putString(facebookInterstitial, value).apply();
    }

    public String getFacebookNative() {
        return appSharedPref.getString(facebookNative, "");
    }

    public void setFacebookNative(String value) {
        prefEditor.putString(facebookNative, value).apply();
    }

    public String getFacebookNativeBanner() {
        return appSharedPref.getString(facebookNativeBanner, "");
    }

    public void setFacebookNativeBanner(String value) {
        prefEditor.putString(facebookNativeBanner, value).apply();
    }

    //=============== Ads Unit - End ===============//


    //=============== Server Data - Start ===============//

    public boolean getServerFlag() {
        return appSharedPref.getBoolean(serverFlag, false);
    }

    public void setServerFlag(boolean value) {
        prefEditor.putBoolean(serverFlag, value).apply();
    }

    public String getServerBaseUrl() {
        return appSharedPref.getString(serverBaseUrl, "");
    }

    public void setServerBaseUrl(String value) {
        prefEditor.putString(serverBaseUrl, value).apply();
    }

    public String getServerCarrierid() {
        return appSharedPref.getString(serverCarrierid, "");
    }

    public void setServerCarrierid(String value) {
        prefEditor.putString(serverCarrierid, value).apply();
    }

    public String getServerCountry() {
        return appSharedPref.getString(serverCountry, "");
    }

    public void setServerCountry(String value) {
        prefEditor.putString(serverCountry, value).apply();
    }

    public String getServerConnectCountryList() {
        return appSharedPref.getString(serverConnectCountryList, "");
    }

    public void setServerConnectCountryList(String value) {
        prefEditor.putString(serverConnectCountryList, value).apply();
    }

    public boolean getConnectServerOnSplash() {
        return appSharedPref.getBoolean(connectServerOnSplash, false);
    }

    public void setConnectServerOnSplash(boolean value) {
        prefEditor.putBoolean(connectServerOnSplash, value).apply();
    }

    //=============== Server Data - End ===============//

}