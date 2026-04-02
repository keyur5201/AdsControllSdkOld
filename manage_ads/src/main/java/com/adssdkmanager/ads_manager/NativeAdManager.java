package com.adssdkmanager.ads_manager;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import com.adssdkmanager.R;
import com.adssdkmanager.common.CommonAppMethodes;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.loadData.AdsPreferences;
import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class NativeAdManager {

    Activity activity;

    Dialog loading;

    public NativeAdManager(Activity activity) {
        this.activity = activity;
    }


    //============= NativeAd Preload ===============//
    //============= NativeAd Preload ===============//

    public void show_NativeAd(ViewGroup ad_frameLayout) {
        if (CommonData.checkAppUpdate(activity) || new AdsPreferences(activity).getAdsOnFlag()) {
            showDialog();
            View view = LayoutInflater.from(activity).inflate(R.layout.shimmer_native_big, ad_frameLayout, false);
            ad_frameLayout.addView(view);
            if (CommonData.isNativePreload(activity)) {
                if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                    populateAmNativeBigAds(ad_frameLayout);
                } else {
                    inflateFbNativeBigAds(ad_frameLayout);
                }
            } else {
                if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                    admob_NativeAd_load(ad_frameLayout);
                } else {
                    fb_NativeAd_load(ad_frameLayout);
                }
            }
        } else {
            ad_frameLayout.setVisibility(View.GONE);
        }
    }

    public void admob_NativeAd_load(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> admob_NativeAd_load() -> ";
        CommonData.LogNative(logMsg + "Start");

        AdLoader.Builder builder = new AdLoader.Builder(activity, new AdsPreferences(activity).getAdmobNative());
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                CommonData.LogNative(logMsg + "onNativeAdLoaded");

                CommonData.google_NativeAds = nativeAd;

                if (!CommonData.isNativePreload(activity)) {
                    populateAmNativeBigAds(ad_frameLayout);
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
                CommonData.LogNative(logMsg + "onAdImpression");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                CommonData.LogNative(logMsg + "onAdOpened");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                CommonData.LogNative(logMsg + "onAdFailedToLoad");
                CommonData.google_NativeAds = null;
                if (CommonData.checkNativeAdUnit(activity)) {
                    if (CommonData.is_Google_Facebook) {
                        fb_NativeAd_load(ad_frameLayout);
                    } else {
                        if (!new AdsPreferences(activity).getNativePreload()) {
                            dismissDialog();
                        }
                    }
                } else {
                    adx_NativeAd_load(ad_frameLayout);
                }
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void adx_NativeAd_load(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> adx_NativeAd_load() -> ";
        CommonData.LogNative(logMsg + "Start");

        AdLoader.Builder builder = new AdLoader.Builder(activity, new AdsPreferences(activity).getAdxNative());
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                CommonData.LogNative(logMsg + "onNativeAdLoaded");

                CommonData.google_NativeAds = nativeAd;

                if (!CommonData.isNativePreload(activity)) {
                    populateAmNativeBigAds(ad_frameLayout);
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
                CommonData.LogNative(logMsg + "onAdImpression");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                CommonData.LogNative(logMsg + "onAdOpened");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                CommonData.LogNative(logMsg + "onAdFailedToLoad");
                CommonData.google_NativeAds = null;
                if (CommonData.is_Google_Facebook) {
                    fb_NativeAd_load(ad_frameLayout);
                } else {
                    if (!new AdsPreferences(activity).getNativePreload()) {
                        dismissDialog();
                    }
                }
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void fb_NativeAd_load(ViewGroup ad_frameLayout) {
        if (CommonData.facebook_nativeAd_1 != null) {
            fb_NativeAd_load_2(ad_frameLayout);
        } else {
            CommonData.LogStartNative();
            String logMsg = CommonData.ads_Priority + " -> fb_NativeAd_load() -> ";
            CommonData.LogNative(logMsg + "Start");

            CommonData.facebook_nativeAd_1 = new NativeAd(activity, new AdsPreferences(activity).getFacebookNative());
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    CommonData.LogNative(logMsg + "onMediaDownloaded");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    CommonData.LogNative(logMsg + "onError");
                    CommonData.facebook_nativeAd_1 = null;
                    fb_NativeAd_load_2(ad_frameLayout);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (CommonData.facebook_nativeAd_1 == null || CommonData.facebook_nativeAd_1 != ad) {
                        return;
                    }
                    CommonData.LogNative(logMsg + "onAdLoaded");

                    if (!CommonData.isNativePreload(activity)) {
                        inflateFbNativeBigAds(ad_frameLayout);
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {
                    CommonData.LogNative(logMsg + "onAdClicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    CommonData.LogNative(logMsg + "onLoggingImpression");
                }
            };

            CommonData.facebook_nativeAd_1.loadAd(CommonData.facebook_nativeAd_1.buildLoadAdConfig().withAdListener(nativeAdListener).build());
        }
    }

    public void fb_NativeAd_load_2(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> fb_NativeAd_load_2() -> ";
        CommonData.LogNative(logMsg + "Start");

        CommonData.facebook_nativeAd_2 = new NativeAd(activity, new AdsPreferences(activity).getFacebookNative());
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                CommonData.LogNative(logMsg + "onMediaDownloaded");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                CommonData.LogNative(logMsg + "onError");
                CommonData.facebook_nativeAd_2 = null;
                if (CommonData.is_Facebook_Google) {
                    admob_NativeAd_load(ad_frameLayout);
                } else {
                    if (!new AdsPreferences(activity).getNativePreload()) {
                        dismissDialog();
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (CommonData.facebook_nativeAd_2 == null || CommonData.facebook_nativeAd_2 != ad) {
                    return;
                }
                CommonData.LogNative(logMsg + "onAdLoaded");

                if (!CommonData.isNativePreload(activity)) {
                    inflateFbNativeBigAds(ad_frameLayout);
                }

            }

            @Override
            public void onAdClicked(Ad ad) {
                CommonData.LogNative(logMsg + "onAdClicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                CommonData.LogNative(logMsg + "onLoggingImpression");
            }
        };

        CommonData.facebook_nativeAd_2.loadAd(CommonData.facebook_nativeAd_2.buildLoadAdConfig().withAdListener(nativeAdListener).build());
    }


    public void show_NativeBannerAd(ViewGroup ad_frameLayout) {
        if (CommonData.checkAppUpdate(activity) || new AdsPreferences(activity).getAdsOnFlag()) {
            showDialog();
            View view = LayoutInflater.from(activity).inflate(R.layout.shimmer_native_banner, ad_frameLayout, false);
            ad_frameLayout.addView(view);
            if (CommonData.isNativePreload(activity)) {
                if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                    populateAmNativeBannerAds(ad_frameLayout);
                } else {
                    inflateFbNativeBannerAds(ad_frameLayout);
                }
            } else {
                if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                    admob_NativeBannerAd_load(ad_frameLayout);
                } else {
                    fb_NativeBannerAd_load(ad_frameLayout);
                }
            }
        } else {
            ad_frameLayout.setVisibility(View.GONE);
        }
    }

    public void admob_NativeBannerAd_load(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> admob_NativeAd_load() -> ";
        CommonData.LogNative(logMsg + "Start");

        AdLoader.Builder builder = new AdLoader.Builder(activity, new AdsPreferences(activity).getAdmobNative());
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                CommonData.LogNative(logMsg + "onNativeAdLoaded");

                CommonData.google_NativeBannerAds = nativeAd;

                if (!CommonData.isNativePreload(activity)) {
                    populateAmNativeBannerAds(ad_frameLayout);
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
                CommonData.LogNative(logMsg + "onAdImpression");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                CommonData.LogNative(logMsg + "onAdOpened");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                CommonData.LogNative(logMsg + "onAdFailedToLoad");
                CommonData.google_NativeBannerAds = null;
                if (CommonData.checkNativebannerAdUnit(activity)) {
                    if (CommonData.is_Google_Facebook) {
                        fb_NativeBannerAd_load(ad_frameLayout);
                    } else {
                        if (!new AdsPreferences(activity).getNativePreload()) {
                            dismissDialog();
                        }
                    }
                } else {
                    adx_NativeBannerAd_load(ad_frameLayout);
                }
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void adx_NativeBannerAd_load(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> adx_NativeAd_load() -> ";
        CommonData.LogNative(logMsg + "Start");

        AdLoader.Builder builder = new AdLoader.Builder(activity, new AdsPreferences(activity).getAdxNative());
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                CommonData.LogNative(logMsg + "onNativeAdLoaded");

                CommonData.google_NativeBannerAds = nativeAd;

                if (!CommonData.isNativePreload(activity)) {
                    populateAmNativeBannerAds(ad_frameLayout);
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
                CommonData.LogNative(logMsg + "onAdImpression");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                CommonData.LogNative(logMsg + "onAdOpened");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                CommonData.LogNative(logMsg + "onAdFailedToLoad");
                CommonData.google_NativeBannerAds = null;
                if (CommonData.is_Google_Facebook) {
                    fb_NativeBannerAd_load(ad_frameLayout);
                } else {
                    if (!new AdsPreferences(activity).getNativePreload()) {
                        dismissDialog();
                    }
                }
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void fb_NativeBannerAd_load(ViewGroup ad_frameLayout) {
        if (CommonData.facebook_nativeBannerAd_1 != null) {
            fb_NativeBannerAd_load_2(ad_frameLayout);
        } else {
            CommonData.LogStartNative();
            String logMsg = CommonData.ads_Priority + " -> fb_NativeBannerAd_load() -> ";
            CommonData.LogNative(logMsg + "Start");

            CommonData.facebook_nativeBannerAd_1 = new NativeAd(activity, new AdsPreferences(activity).getFacebookNative());
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    CommonData.LogNative(logMsg + "onMediaDownloaded");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    CommonData.LogNative(logMsg + "onError");
                    CommonData.facebook_nativeBannerAd_1 = null;
                    fb_NativeBannerAd_load_2(ad_frameLayout);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (CommonData.facebook_nativeBannerAd_1 == null || CommonData.facebook_nativeBannerAd_1 != ad) {
                        return;
                    }
                    CommonData.LogNative(logMsg + "onAdLoaded");

                    if (!CommonData.isNativePreload(activity)) {
                        inflateFbNativeBannerAds(ad_frameLayout);
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {
                    CommonData.LogNative(logMsg + "onAdClicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    CommonData.LogNative(logMsg + "onLoggingImpression");
                }
            };

            CommonData.facebook_nativeBannerAd_1.loadAd(CommonData.facebook_nativeBannerAd_1.buildLoadAdConfig().withAdListener(nativeAdListener).build());
        }
    }

    public void fb_NativeBannerAd_load_2(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> fb_NativeBannerAd_load_2() -> ";
        CommonData.LogNative(logMsg + "Start");

        CommonData.facebook_nativeBannerAd_2 = new NativeAd(activity, new AdsPreferences(activity).getFacebookNative());
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                CommonData.LogNative(logMsg + "onMediaDownloaded");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                CommonData.LogNative(logMsg + "onError");
                CommonData.facebook_nativeBannerAd_2 = null;
                if (CommonData.is_Facebook_Google) {
                    admob_NativeBannerAd_load(ad_frameLayout);
                } else {
                    if (!new AdsPreferences(activity).getNativePreload()) {
                        dismissDialog();
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (CommonData.facebook_nativeBannerAd_2 == null || CommonData.facebook_nativeBannerAd_2 != ad) {
                    return;
                }
                CommonData.LogNative(logMsg + "onAdLoaded");

                if (!CommonData.isNativePreload(activity)) {
                    inflateFbNativeBannerAds(ad_frameLayout);
                }

            }

            @Override
            public void onAdClicked(Ad ad) {
                CommonData.LogNative(logMsg + "onAdClicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                CommonData.LogNative(logMsg + "onLoggingImpression");
            }
        };

        CommonData.facebook_nativeBannerAd_2.loadAd(CommonData.facebook_nativeBannerAd_2.buildLoadAdConfig().withAdListener(nativeAdListener).build());
    }


    public void show_NativeSmallAd(ViewGroup ad_frameLayout) {
        if (CommonData.checkAppUpdate(activity) || new AdsPreferences(activity).getAdsOnFlag()) {
            showDialog();
            View view = LayoutInflater.from(activity).inflate(R.layout.shimmer_native_small, ad_frameLayout, false);
            ad_frameLayout.addView(view);
            if (CommonData.isNativePreload(activity)) {
                if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                    populateAmNativeSmallAds(ad_frameLayout);
                } else {
                    inflateFbNativeSmallAds(ad_frameLayout);
                }
            } else {
                if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                    admob_NativeSmallAd_load(ad_frameLayout);
                } else {
                    fb_NativeSmallAd_load(ad_frameLayout);
                }
            }
        } else {
            ad_frameLayout.setVisibility(View.GONE);
        }
    }

    public void admob_NativeSmallAd_load(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> admob_NativeSmallAd_load() -> ";
        CommonData.LogNative(logMsg + "Start");

        AdLoader.Builder builder = new AdLoader.Builder(activity, new AdsPreferences(activity).getAdmobNative());
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                CommonData.LogNative(logMsg + "onNativeAdLoaded");

                CommonData.google_NativeSmallAds = nativeAd;

                if (!CommonData.isNativePreload(activity)) {
                    populateAmNativeSmallAds(ad_frameLayout);
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
                CommonData.LogNative(logMsg + "onAdImpression");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                CommonData.LogNative(logMsg + "onAdOpened");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                CommonData.LogNative(logMsg + "onAdFailedToLoad");
                CommonData.google_NativeSmallAds = null;
                if (CommonData.checkNativebannerAdUnit(activity)) {
                    if (CommonData.is_Google_Facebook) {
                        fb_NativeSmallAd_load(ad_frameLayout);
                    } else {
                        if (!new AdsPreferences(activity).getNativePreload()) {
                            dismissDialog();
                        }
                    }
                } else {
                    adx_NativeSmallAd_load(ad_frameLayout);
                }
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void adx_NativeSmallAd_load(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> adx_NativeSmallAd_load() -> ";
        CommonData.LogNative(logMsg + "Start");

        AdLoader.Builder builder = new AdLoader.Builder(activity, new AdsPreferences(activity).getAdxNative());
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                CommonData.LogNative(logMsg + "onNativeAdLoaded");

                CommonData.google_NativeSmallAds = nativeAd;

                if (!CommonData.isNativePreload(activity)) {
                    populateAmNativeSmallAds(ad_frameLayout);
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
                CommonData.LogNative(logMsg + "onAdImpression");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                CommonData.LogNative(logMsg + "onAdOpened");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                CommonData.LogNative(logMsg + "onAdFailedToLoad");
                CommonData.google_NativeSmallAds = null;
                if (CommonData.is_Google_Facebook) {
                    fb_NativeSmallAd_load(ad_frameLayout);
                } else {
                    if (!new AdsPreferences(activity).getNativePreload()) {
                        dismissDialog();
                    }
                }
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void fb_NativeSmallAd_load(ViewGroup ad_frameLayout) {
        if (CommonData.facebook_nativeSmallAd_1 != null) {
            fb_NativeSmallAd_load_2(ad_frameLayout);
        } else {
            CommonData.LogStartNative();
            String logMsg = CommonData.ads_Priority + " -> fb_NativeSmallAd_load() -> ";
            CommonData.LogNative(logMsg + "Start");

            CommonData.facebook_nativeSmallAd_1 = new NativeBannerAd(activity, new AdsPreferences(activity).getFacebookNative());
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    CommonData.LogNative(logMsg + "onMediaDownloaded");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    CommonData.LogNative(logMsg + "onError");
                    CommonData.facebook_nativeSmallAd_1 = null;
                    fb_NativeSmallAd_load_2(ad_frameLayout);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (CommonData.facebook_nativeSmallAd_1 == null || CommonData.facebook_nativeSmallAd_1 != ad) {
                        return;
                    }
                    CommonData.LogNative(logMsg + "onAdLoaded");

                    if (!CommonData.isNativePreload(activity)) {
                        inflateFbNativeSmallAds(ad_frameLayout);
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {
                    CommonData.LogNative(logMsg + "onAdClicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    CommonData.LogNative(logMsg + "onLoggingImpression");
                }
            };

            CommonData.facebook_nativeSmallAd_1.loadAd(CommonData.facebook_nativeSmallAd_1.buildLoadAdConfig().withAdListener(nativeAdListener).build());
        }
    }

    public void fb_NativeSmallAd_load_2(ViewGroup ad_frameLayout) {
        CommonData.LogStartNative();
        String logMsg = CommonData.ads_Priority + " -> fb_NativeSmallAd_load_2() -> ";
        CommonData.LogNative(logMsg + "Start");

        CommonData.facebook_nativeSmallAd_2 = new NativeBannerAd(activity, new AdsPreferences(activity).getFacebookNative());
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                CommonData.LogNative(logMsg + "onMediaDownloaded");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                CommonData.LogNative(logMsg + "onError");
                CommonData.facebook_nativeSmallAd_2 = null;
                if (CommonData.is_Facebook_Google) {
                    admob_NativeSmallAd_load(ad_frameLayout);
                } else {
                    if (!new AdsPreferences(activity).getNativePreload()) {
                        dismissDialog();
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (CommonData.facebook_nativeSmallAd_2 == null || CommonData.facebook_nativeSmallAd_2 != ad) {
                    return;
                }
                CommonData.LogNative(logMsg + "onAdLoaded");

                if (!CommonData.isNativePreload(activity)) {
                    inflateFbNativeSmallAds(ad_frameLayout);
                }

            }

            @Override
            public void onAdClicked(Ad ad) {
                CommonData.LogNative(logMsg + "onAdClicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                CommonData.LogNative(logMsg + "onLoggingImpression");
            }
        };

        CommonData.facebook_nativeSmallAd_2.loadAd(CommonData.facebook_nativeSmallAd_2.buildLoadAdConfig().withAdListener(nativeAdListener).build());
    }


    //=============== Populate Google & Facebook Native Ads ===================
    //=============== Populate Google & Facebook Native Ads ===================

    public void populateAmNativeBigAds(ViewGroup ad_frameLayout) {
        if (CommonData.google_NativeAds != null) {
            dismissDialog();

            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.am_native_big, null);
            ad_frameLayout.removeAllViews();
            ad_frameLayout.addView(adView);

            LinearLayout adContainer = adView.findViewById(R.id.adContainer);
            LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);
            com.google.android.gms.ads.nativead.MediaView ad_media = adView.findViewById(R.id.ad_media);
            AppCompatImageView ad_app_icon = adView.findViewById(R.id.ad_app_icon);
            AppCompatTextView ad_adAttribution = adView.findViewById(R.id.ad_adAttribution);
            AppCompatTextView ad_advertiser = adView.findViewById(R.id.ad_advertiser);
            AppCompatTextView ad_headline = adView.findViewById(R.id.ad_headline);
            AppCompatTextView ad_body = adView.findViewById(R.id.ad_body);
            AppCompatButton ad_call_to_action = adView.findViewById(R.id.ad_call_to_action);

            adContainer.setVisibility(View.GONE);
            loadingContainer.setVisibility(View.VISIBLE);

            adView.setMediaView(ad_media);
            adView.setHeadlineView(ad_headline);
            adView.setBodyView(ad_body);
            adView.setCallToActionView(ad_call_to_action);
            adView.setIconView(ad_app_icon);
            adView.setAdvertiserView(ad_advertiser);

            if (CommonData.isAdsButtonColorFlag(activity)) {
                Drawable tintDr = DrawableCompat.wrap(Objects.requireNonNull(adView.getCallToActionView()).getBackground());
                DrawableCompat.setTint(tintDr, Color.parseColor(new AdsPreferences(activity).getAdsButtonColor()));
                adView.getCallToActionView().setBackground(tintDr);
            }

            Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(CommonData.google_NativeAds.getMediaContent()));


            if (CommonData.google_NativeAds.getHeadline() == null) {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(CommonData.google_NativeAds.getHeadline());

                adContainer.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.GONE);
            }

            if (CommonData.google_NativeAds.getBody() == null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(CommonData.google_NativeAds.getBody());
            }

            if (CommonData.google_NativeAds.getCallToAction() == null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                ((AppCompatButton) adView.getCallToActionView()).setText(CommonData.google_NativeAds.getCallToAction());
            }

            //   HideAds: Icon
            if (CommonData.google_NativeAds.getIcon() == null) {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
            } else {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.VISIBLE);
                ((AppCompatImageView) adView.getIconView()).setImageDrawable(CommonData.google_NativeAds.getIcon().getDrawable());
            }

            if (CommonData.google_NativeAds.getAdvertiser() == null) {
                Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.GONE);
            } else {
                ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(CommonData.google_NativeAds.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            adView.setNativeAd(CommonData.google_NativeAds);
            com.google.android.gms.ads.VideoController vc = CommonData.google_NativeAds.getMediaContent().getVideoController();
            if (vc.hasVideoContent()) {
                vc.setVideoLifecycleCallbacks(new com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }
        } else {
            if (CommonData.is_Google_Facebook) {
                inflateFbNativeBigAds(ad_frameLayout);
            } else {
                dismissDialog();
            }
        }
    }

    public void populateAmNativeBannerAds(ViewGroup ad_frameLayout) {
        if (CommonData.google_NativeBannerAds != null) {
            dismissDialog();

            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.am_native_banner, null);
            ad_frameLayout.removeAllViews();
            ad_frameLayout.addView(adView);

            LinearLayout adContainer = adView.findViewById(R.id.adContainer);
            LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);
            com.google.android.gms.ads.nativead.MediaView ad_media = adView.findViewById(R.id.ad_media);
            AppCompatImageView ad_app_icon = adView.findViewById(R.id.ad_app_icon);
            AppCompatTextView ad_adAttribution = adView.findViewById(R.id.ad_adAttribution);
            AppCompatTextView ad_advertiser = adView.findViewById(R.id.ad_advertiser);
            AppCompatTextView ad_headline = adView.findViewById(R.id.ad_headline);
            AppCompatTextView ad_body = adView.findViewById(R.id.ad_body);
            AppCompatButton ad_call_to_action = adView.findViewById(R.id.ad_call_to_action);

            adContainer.setVisibility(View.GONE);
            loadingContainer.setVisibility(View.VISIBLE);

            adView.setMediaView(ad_media);
            adView.setHeadlineView(ad_headline);
            adView.setBodyView(ad_body);
            adView.setCallToActionView(ad_call_to_action);
            adView.setIconView(ad_app_icon);
            adView.setAdvertiserView(ad_advertiser);

            if (CommonData.isAdsButtonColorFlag(activity)) {
                Drawable tintDr = DrawableCompat.wrap(Objects.requireNonNull(adView.getCallToActionView()).getBackground());
                DrawableCompat.setTint(tintDr, Color.parseColor(new AdsPreferences(activity).getAdsButtonColor()));
                adView.getCallToActionView().setBackground(tintDr);
            }

            Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(CommonData.google_NativeBannerAds.getMediaContent()));


            if (CommonData.google_NativeBannerAds.getHeadline() == null) {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(CommonData.google_NativeBannerAds.getHeadline());

                adContainer.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.GONE);
            }

            if (CommonData.google_NativeBannerAds.getBody() == null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(CommonData.google_NativeBannerAds.getBody());
            }

            if (CommonData.google_NativeBannerAds.getCallToAction() == null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                ((AppCompatButton) adView.getCallToActionView()).setText(CommonData.google_NativeBannerAds.getCallToAction());
            }

            //   HideAds: Icon
            if (CommonData.google_NativeBannerAds.getIcon() == null) {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
            } else {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.VISIBLE);
                ((AppCompatImageView) adView.getIconView()).setImageDrawable(CommonData.google_NativeBannerAds.getIcon().getDrawable());
            }

            if (CommonData.google_NativeBannerAds.getAdvertiser() == null) {
                Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.GONE);
            } else {
                ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(CommonData.google_NativeBannerAds.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            adView.setNativeAd(CommonData.google_NativeBannerAds);
            com.google.android.gms.ads.VideoController vc = CommonData.google_NativeBannerAds.getMediaContent().getVideoController();
            if (vc.hasVideoContent()) {
                vc.setVideoLifecycleCallbacks(new com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }
        } else {
            if (CommonData.is_Google_Facebook) {
                inflateFbNativeBannerAds(ad_frameLayout);
            } else {
                dismissDialog();
            }
        }
    }

    public void populateAmNativeSmallAds(ViewGroup ad_frameLayout) {
        if (CommonData.google_NativeSmallAds != null) {
            dismissDialog();

            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.am_native_small, null);
            ad_frameLayout.removeAllViews();
            ad_frameLayout.addView(adView);

            LinearLayout adContainer = adView.findViewById(R.id.adContainer);
            LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);
            AppCompatImageView ad_app_icon = adView.findViewById(R.id.ad_app_icon);
            AppCompatTextView ad_adAttribution = adView.findViewById(R.id.ad_adAttribution);
            AppCompatTextView ad_headline = adView.findViewById(R.id.ad_headline);
            AppCompatTextView ad_body = adView.findViewById(R.id.ad_body);
            AppCompatButton ad_call_to_action = adView.findViewById(R.id.ad_call_to_action);

            adContainer.setVisibility(View.GONE);
            loadingContainer.setVisibility(View.VISIBLE);

            adView.setHeadlineView(ad_headline);
            adView.setBodyView(ad_body);
            adView.setCallToActionView(ad_call_to_action);
            adView.setIconView(ad_app_icon);

            if (CommonData.isAdsButtonColorFlag(activity)) {
                Drawable tintDr = DrawableCompat.wrap(Objects.requireNonNull(adView.getCallToActionView()).getBackground());
                DrawableCompat.setTint(tintDr, Color.parseColor(new AdsPreferences(activity).getAdsButtonColor()));
                adView.getCallToActionView().setBackground(tintDr);
            }

            if (CommonData.google_NativeSmallAds.getHeadline() == null) {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(CommonData.google_NativeSmallAds.getHeadline());

                adContainer.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.GONE);
            }

            if (CommonData.google_NativeSmallAds.getBody() == null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(CommonData.google_NativeSmallAds.getBody());
            }

            if (CommonData.google_NativeSmallAds.getCallToAction() == null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                ((AppCompatButton) adView.getCallToActionView()).setText(CommonData.google_NativeSmallAds.getCallToAction());
            }

            //   HideAds: Icon
            if (CommonData.google_NativeSmallAds.getIcon() == null) {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
            } else {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.VISIBLE);
                ((AppCompatImageView) adView.getIconView()).setImageDrawable(CommonData.google_NativeSmallAds.getIcon().getDrawable());
            }

            adView.setNativeAd(CommonData.google_NativeSmallAds);
            com.google.android.gms.ads.VideoController vc = CommonData.google_NativeSmallAds.getMediaContent().getVideoController();
            if (vc.hasVideoContent()) {
                vc.setVideoLifecycleCallbacks(new com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }
        } else {
            if (CommonData.is_Google_Facebook) {
                inflateFbNativeSmallAds(ad_frameLayout);
            } else {
                dismissDialog();
            }
        }
    }

    public void inflateFbNativeBigAds(ViewGroup ad_frameLayout) {
        if (CommonData.facebook_nativeAd_1 != null) {
            inFbNativeBigAds(CommonData.facebook_nativeAd_1, ad_frameLayout);
        } else if (CommonData.facebook_nativeAd_2 != null) {
            inFbNativeBigAds(CommonData.facebook_nativeAd_2, ad_frameLayout);
        } else {
            if (CommonData.is_Facebook_Google) {
                populateAmNativeBigAds(ad_frameLayout);
            } else {
                dismissDialog();
            }
        }
    }

    public void inFbNativeBigAds(NativeAd fbNativeAd, ViewGroup ad_frameLayout) {
        dismissDialog();

        fbNativeAd.unregisterView();

        View adView = LayoutInflater.from(activity).inflate(R.layout.fb_native_big, ad_frameLayout, false);

        ad_frameLayout.removeAllViews();
        ad_frameLayout.addView(adView);

        NativeAdLayout nativeAdLay = adView.findViewById(R.id.nativeAdLay);
        LinearLayout adContainer = adView.findViewById(R.id.adContainer);
        MediaView native_ad_icon = adView.findViewById(R.id.native_ad_icon);
        AppCompatTextView native_ad_title = adView.findViewById(R.id.native_ad_title);
        LinearLayout ad_choices_container = adView.findViewById(R.id.ad_choices_container);
        AppCompatTextView native_ad_sponsored_label = adView.findViewById(R.id.native_ad_sponsored_label);
        AppCompatTextView native_ad_social_context = adView.findViewById(R.id.native_ad_social_context);
        MediaView native_ad_media = adView.findViewById(R.id.native_ad_media);
        AppCompatTextView native_ad_body = adView.findViewById(R.id.native_ad_body);
        AppCompatButton native_ad_call_to_action = adView.findViewById(R.id.native_ad_call_to_action);
        LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);

        adContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);

        com.facebook.ads.AdOptionsView adOptionsView = new com.facebook.ads.AdOptionsView(activity, fbNativeAd, nativeAdLay);
        ad_choices_container.removeAllViews();

        if (CommonData.isAdsButtonColorFlag(activity)) {
            Drawable tintDr = DrawableCompat.wrap(native_ad_call_to_action.getBackground());
            DrawableCompat.setTint(tintDr, Color.parseColor(new AdsPreferences(activity).getAdsButtonColor()));
            native_ad_call_to_action.setBackground(tintDr);
        }

        if (native_ad_title != null) {
            ad_choices_container.addView(adOptionsView, 0);

            adContainer.setVisibility(View.VISIBLE);
            loadingContainer.setVisibility(View.GONE);
        }

        native_ad_title.setText(fbNativeAd.getAdvertiserName());
        native_ad_body.setText(fbNativeAd.getAdBodyText());
        native_ad_social_context.setText(fbNativeAd.getAdSocialContext());
        native_ad_call_to_action.setVisibility(fbNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        native_ad_call_to_action.setText(fbNativeAd.getAdCallToAction());
        native_ad_sponsored_label.setText(fbNativeAd.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(native_ad_icon);
        clickableViews.add(native_ad_title);
        clickableViews.add(ad_choices_container);
        clickableViews.add(native_ad_sponsored_label);
        clickableViews.add(native_ad_social_context);
        clickableViews.add(native_ad_media);
        clickableViews.add(native_ad_body);
        clickableViews.add(native_ad_call_to_action);

        fbNativeAd.registerViewForInteraction(adView, native_ad_media, native_ad_icon, clickableViews);
    }

    public void inflateFbNativeBannerAds(ViewGroup ad_frameLayout) {
        if (CommonData.facebook_nativeBannerAd_1 != null) {
            inFbNativeBannerAds(CommonData.facebook_nativeBannerAd_1, ad_frameLayout);
        } else if (CommonData.facebook_nativeBannerAd_2 != null) {
            inFbNativeBannerAds(CommonData.facebook_nativeBannerAd_2, ad_frameLayout);
        } else {
            if (CommonData.is_Facebook_Google) {
                populateAmNativeBannerAds(ad_frameLayout);
            } else {
                dismissDialog();
            }
        }
    }

    public void inFbNativeBannerAds(NativeAd fb_NativeBannerAd, ViewGroup ad_frameLayout) {
        dismissDialog();

        fb_NativeBannerAd.unregisterView();

        View adView = LayoutInflater.from(activity).inflate(R.layout.fb_native_banner, ad_frameLayout, false);

        ad_frameLayout.removeAllViews();
        ad_frameLayout.addView(adView);

        NativeAdLayout nativeAdLay = adView.findViewById(R.id.nativeAdLay);
        LinearLayout adContainer = adView.findViewById(R.id.adContainer);
        MediaView native_icon_view = adView.findViewById(R.id.native_icon_view);
        AppCompatTextView native_ad_title = adView.findViewById(R.id.native_ad_title);
        LinearLayout ad_choices_container = adView.findViewById(R.id.ad_choices_container);
        AppCompatTextView native_ad_sponsored_label = adView.findViewById(R.id.native_ad_sponsored_label);
        AppCompatTextView native_ad_social_context = adView.findViewById(R.id.native_ad_social_context);
        MediaView native_ad_media = adView.findViewById(R.id.native_ad_media);
        AppCompatTextView native_ad_body = adView.findViewById(R.id.native_ad_body);
        AppCompatButton native_ad_call_to_action = adView.findViewById(R.id.native_ad_call_to_action);
        LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);

        adContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);

        com.facebook.ads.AdOptionsView adOptionsView = new com.facebook.ads.AdOptionsView(activity, fb_NativeBannerAd, nativeAdLay);
        ad_choices_container.removeAllViews();

        if (CommonData.isAdsButtonColorFlag(activity)) {
            Drawable tintDr = DrawableCompat.wrap(native_ad_call_to_action.getBackground());
            DrawableCompat.setTint(tintDr, Color.parseColor(new AdsPreferences(activity).getAdsButtonColor()));
            native_ad_call_to_action.setBackground(tintDr);
        }

        if (native_ad_title != null) {
            ad_choices_container.addView(adOptionsView, 0);

            adContainer.setVisibility(View.VISIBLE);
            loadingContainer.setVisibility(View.GONE);
        }

        native_ad_title.setText(fb_NativeBannerAd.getAdvertiserName());
        native_ad_body.setText(fb_NativeBannerAd.getAdBodyText());
        native_ad_social_context.setText(fb_NativeBannerAd.getAdSocialContext());
        native_ad_call_to_action.setVisibility(fb_NativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        native_ad_call_to_action.setText(fb_NativeBannerAd.getAdCallToAction());
        native_ad_sponsored_label.setText(fb_NativeBannerAd.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(native_icon_view);
        clickableViews.add(native_ad_title);
        clickableViews.add(ad_choices_container);
        clickableViews.add(native_ad_sponsored_label);
        clickableViews.add(native_ad_social_context);
        clickableViews.add(native_ad_media);
        clickableViews.add(native_ad_body);
        clickableViews.add(native_ad_call_to_action);

        fb_NativeBannerAd.registerViewForInteraction(adView, native_ad_media, native_icon_view, clickableViews);
    }

    public void inflateFbNativeSmallAds(ViewGroup ad_frameLayout) {
        if (CommonData.facebook_nativeSmallAd_1 != null) {
            inFbNativeSmallAds(CommonData.facebook_nativeSmallAd_1, ad_frameLayout);
        } else if (CommonData.facebook_nativeSmallAd_2 != null) {
            inFbNativeSmallAds(CommonData.facebook_nativeSmallAd_2, ad_frameLayout);
        } else {
            if (CommonData.is_Facebook_Google) {
                populateAmNativeSmallAds(ad_frameLayout);
            } else {
                dismissDialog();
            }
        }
    }

    public void inFbNativeSmallAds(NativeBannerAd fb_NativeSmallAd, ViewGroup ad_frameLayout) {
        dismissDialog();

        fb_NativeSmallAd.unregisterView();

        View adView = LayoutInflater.from(activity).inflate(R.layout.fb_native_small, ad_frameLayout, false);

        ad_frameLayout.removeAllViews();
        ad_frameLayout.addView(adView);

        NativeAdLayout nativeAdLay = adView.findViewById(R.id.nativeAdLay);
        LinearLayout adContainer = adView.findViewById(R.id.adContainer);
        MediaView native_icon_view = adView.findViewById(R.id.native_icon_view);
        AppCompatTextView native_ad_title = adView.findViewById(R.id.native_ad_title);
        LinearLayout ad_choices_container = adView.findViewById(R.id.ad_choices_container);
        AppCompatTextView native_ad_sponsored_label = adView.findViewById(R.id.native_ad_sponsored_label);
        AppCompatTextView native_ad_social_context = adView.findViewById(R.id.native_ad_social_context);
        AppCompatTextView native_ad_body = adView.findViewById(R.id.native_ad_body);
        AppCompatButton native_ad_call_to_action = adView.findViewById(R.id.native_ad_call_to_action);
        LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);

        adContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);

        com.facebook.ads.AdOptionsView adOptionsView = new com.facebook.ads.AdOptionsView(activity, fb_NativeSmallAd, nativeAdLay);
        ad_choices_container.removeAllViews();

        if (CommonData.isAdsButtonColorFlag(activity)) {
            Drawable tintDr = DrawableCompat.wrap(native_ad_call_to_action.getBackground());
            DrawableCompat.setTint(tintDr, Color.parseColor(new AdsPreferences(activity).getAdsButtonColor()));
            native_ad_call_to_action.setBackground(tintDr);
        }

        if (native_ad_title != null) {
            ad_choices_container.addView(adOptionsView, 0);

            adContainer.setVisibility(View.VISIBLE);
            loadingContainer.setVisibility(View.GONE);
        }

        native_ad_title.setText(fb_NativeSmallAd.getAdvertiserName());
        native_ad_body.setText(fb_NativeSmallAd.getAdBodyText());
        native_ad_social_context.setText(fb_NativeSmallAd.getAdSocialContext());
        native_ad_call_to_action.setVisibility(fb_NativeSmallAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        native_ad_call_to_action.setText(fb_NativeSmallAd.getAdCallToAction());
        native_ad_sponsored_label.setText(fb_NativeSmallAd.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(native_icon_view);
        clickableViews.add(native_ad_title);
        clickableViews.add(ad_choices_container);
        clickableViews.add(native_ad_sponsored_label);
        clickableViews.add(native_ad_social_context);
        clickableViews.add(native_ad_body);
        clickableViews.add(native_ad_call_to_action);

        fb_NativeSmallAd.registerViewForInteraction(adView, native_icon_view, clickableViews);
    }


    public void showQurekaAds(ViewGroup ad_frameLayout) {
        if (CommonData.isShowOtherAds(activity)) {
            ad_frameLayout.setVisibility(View.VISIBLE);

            View adView = activity.getLayoutInflater().inflate(R.layout.qu_native, null);

            ConstraintLayout qureka1, qureka2, qureka3;
            ImageView quIcon1, quMedia1, quIcon2, quMedia2, quMedia3, quIcon3;
            TextView quHeadline1, quBody1, quHeadline2, quBody2, quHeadline3, quBody3;
            AppCompatButton quCta1, quCta2, quCta3;

            qureka1 = adView.findViewById(R.id.qureka1);
            qureka2 = adView.findViewById(R.id.qureka2);
            qureka3 = adView.findViewById(R.id.qureka3);
            quIcon1 = adView.findViewById(R.id.quIcon1);
            quMedia1 = adView.findViewById(R.id.quMedia1);
            quIcon2 = adView.findViewById(R.id.quIcon2);
            quMedia2 = adView.findViewById(R.id.quMedia2);
            quMedia3 = adView.findViewById(R.id.quMedia3);
            quIcon3 = adView.findViewById(R.id.quIcon3);
            quHeadline1 = adView.findViewById(R.id.quHeadline1);
            quBody1 = adView.findViewById(R.id.quBody1);
            quHeadline2 = adView.findViewById(R.id.quHeadline2);
            quBody2 = adView.findViewById(R.id.quBody2);
            quHeadline3 = adView.findViewById(R.id.quHeadline3);
            quBody3 = adView.findViewById(R.id.quBody3);
            quCta1 = adView.findViewById(R.id.quCta1);
            quCta2 = adView.findViewById(R.id.quCta2);
            quCta3 = adView.findViewById(R.id.quCta3);

            List<Integer> laySizeList = new ArrayList<Integer>();
            for (int i = 1; i <= 3; i++) {
                laySizeList.add(i);
            }

            int randomNumber = laySizeList.get(new Random().nextInt(laySizeList.size()));

            if (randomNumber == 1) {
                qureka1.setVisibility(View.VISIBLE);
                qureka2.setVisibility(View.GONE);
                qureka3.setVisibility(View.GONE);
                setQurekaData(randomNumber, quIcon1, quMedia1, quHeadline1, quBody1, quCta1);
            } else if (randomNumber == 2) {
                qureka1.setVisibility(View.GONE);
                qureka2.setVisibility(View.VISIBLE);
                qureka3.setVisibility(View.GONE);
                setQurekaData(randomNumber, quIcon2, quMedia2, quHeadline2, quBody2, quCta2);
            } else if (randomNumber == 3) {
                qureka1.setVisibility(View.GONE);
                qureka2.setVisibility(View.GONE);
                qureka3.setVisibility(View.VISIBLE);
                setQurekaData(randomNumber, quIcon3, quMedia3, quHeadline3, quBody3, quCta3);
            } else {
                qureka1.setVisibility(View.VISIBLE);
                qureka2.setVisibility(View.GONE);
                qureka3.setVisibility(View.GONE);
                setQurekaData(randomNumber, quIcon1, quMedia1, quHeadline1, quBody1, quCta1);
            }

            ad_frameLayout.removeAllViews();
            ad_frameLayout.addView(adView);

        } else {
            ad_frameLayout.setVisibility(View.GONE);
        }
    }

    public void setQurekaData(int laySize, ImageView quIcon, ImageView quMedia, TextView quHeadline, TextView quBody, AppCompatButton quCta) {

        if (CommonData.isAdsButtonColorFlag(activity)) {
            Drawable tintDr = DrawableCompat.wrap(quCta.getBackground());
            DrawableCompat.setTint(tintDr, Color.parseColor(new AdsPreferences(activity).getAdsButtonColor()));
            quCta.setBackground(tintDr);
        }

        String[] header = activity.getResources().getStringArray(R.array.header);
        String randomheader = header[new Random().nextInt(header.length)];
        String[] body = activity.getResources().getStringArray(R.array.body);
        String randombody = body[new Random().nextInt(body.length)];

        Glide.with(activity.getApplicationContext())
                .load(new OtherAdsManager(activity).generateAdsUrl(CommonData.other_icon_square_folder)).into(quIcon);

        if (laySize == 3) {
            Glide.with(activity.getApplicationContext())
                    .load(new OtherAdsManager(activity).generateAdsUrl(CommonData.other_icon_square_folder)).into(quMedia);
        } else {
            Glide.with(activity.getApplicationContext())
                    .load(new OtherAdsManager(activity).generateAdsUrl(CommonData.other_media_image_folder)).into(quMedia);
        }

        quHeadline.setText(randomheader);
        quBody.setText(randombody);

        quIcon.setOnClickListener(v -> {
            new CommonAppMethodes(activity).ChromeView(new OtherAdsManager(activity).adsRedirectUrl());
        });
        quMedia.setOnClickListener(v -> {
            new CommonAppMethodes(activity).ChromeView(new OtherAdsManager(activity).adsRedirectUrl());
        });
        quHeadline.setOnClickListener(v -> {
            new CommonAppMethodes(activity).ChromeView(new OtherAdsManager(activity).adsRedirectUrl());
        });
        quBody.setOnClickListener(v -> {
            new CommonAppMethodes(activity).ChromeView(new OtherAdsManager(activity).adsRedirectUrl());
        });
        quCta.setOnClickListener(v -> {
            new CommonAppMethodes(activity).ChromeView(new OtherAdsManager(activity).adsRedirectUrl());
        });

    }

    public void showDialog() {
        if (new AdsPreferences(activity).getShowNativeLoader()) {
            if (loading != null) {
                loading.dismiss();
            }
            loading = new Dialog(activity);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setGravity(Gravity.CENTER);
            loading.setContentView(R.layout.ad_loading);
            loading.show();
        }
    }

    public void dismissDialog() {
        if (new AdsPreferences(activity).getShowNativeLoader()) {
            if (loading != null) {
                loading.dismiss();
            }
        }
    }

}

