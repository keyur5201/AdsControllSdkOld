package com.adssdkmanager.ads_manager;

import android.app.Activity;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

import com.adssdkmanager.R;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.loadData.AdsPreferences;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class BannerAdsLoad {

    Activity activity;
    public boolean initialLayoutComplete = false;

    public BannerAdsLoad(Activity activity) {
        this.activity = activity;
    }


    //============= Google BannerAds ===============//
    //============= Google BannerAds ===============//

    public void G_Admob_BannerAd(ViewGroup ad_frameLayout) {

        CommonData.LogStartBanner();
        String logMsg = CommonData.ads_Priority + " -> G_Admob_BannerAd() -> ";
        CommonData.LogBanner(logMsg + "Start");

        CommonData.google_BannerAds = new AdView(activity);
        ad_frameLayout.addView(CommonData.google_BannerAds);
        ad_frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        CommonData.LogBanner(logMsg + "onGlobalLayout");
                        if (!initialLayoutComplete) {
                            initialLayoutComplete = true;
                            CommonData.google_BannerAds.setAdUnitId(new AdsPreferences(activity).getAdmobBanner());
                            AdSize adSize = getAdSize(ad_frameLayout);
                            CommonData.google_BannerAds.setAdSize(adSize);

                            AdRequest adRequest = new AdRequest.Builder().build();
                            CommonData.google_BannerAds.loadAd(adRequest);
                        }
                    }
                });
        CommonData.google_BannerAds.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                CommonData.LogBanner(logMsg + "onAdClicked");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                CommonData.LogBanner(logMsg + "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                CommonData.LogBanner(logMsg + "onAdFailedToLoad");
                CommonData.google_BannerAds = null;
                if (CommonData.checkBannerAdUnit(activity)) {
                    if (CommonData.is_Google_Facebook) {
                        Fb_BannerAd(ad_frameLayout);
                    }
                } else {
                    G_Adx_BannerAd(ad_frameLayout);
                }
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                CommonData.LogBanner(logMsg + "onAdImpression");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                CommonData.LogBanner(logMsg + "onAdLoaded");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                CommonData.LogBanner(logMsg + "onAdOpened");
            }
        });
    }

    public void G_Adx_BannerAd(ViewGroup ad_frameLayout) {

        CommonData.LogStartBanner();
        String logMsg = CommonData.ads_Priority + " -> G_Adx_BannerAd() -> ";
        CommonData.LogBanner(logMsg + "Start");

        CommonData.google_BannerAds = new AdView(activity);
        ad_frameLayout.addView(CommonData.google_BannerAds);
        ad_frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        CommonData.LogBanner(logMsg + "onGlobalLayout");
                        if (!initialLayoutComplete) {
                            initialLayoutComplete = true;
                            CommonData.google_BannerAds.setAdUnitId(new AdsPreferences(activity).getAdxBanner());
                            AdSize adSize = getAdSize(ad_frameLayout);
                            CommonData.google_BannerAds.setAdSize(adSize);

                            AdRequest adRequest = new AdRequest.Builder().build();
                            CommonData.google_BannerAds.loadAd(adRequest);
                        }
                    }
                });
        CommonData.google_BannerAds.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                CommonData.LogBanner(logMsg + "onAdClicked");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                CommonData.LogBanner(logMsg + "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                CommonData.LogBanner(logMsg + "onAdFailedToLoad");
                if (CommonData.is_Google_Facebook) {
                    Fb_BannerAd(ad_frameLayout);
                }
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                CommonData.LogBanner(logMsg + "onAdImpression");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                CommonData.LogBanner(logMsg + "onAdLoaded");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                CommonData.LogBanner(logMsg + "onAdOpened");
            }
        });
    }

    private AdSize getAdSize(ViewGroup ad_frameLayout) {
        WindowMetrics windowMetrics = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
        }

        Rect bounds = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            bounds = windowMetrics.getBounds();
        }

        float adWidthPixels = ad_frameLayout.getWidth();
        if (adWidthPixels == 0f) {
            adWidthPixels = bounds.width();
        }

        float density = activity.getResources().getDisplayMetrics().density;
        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }


    //============= Facebook BannerAds ===============//
    //============= Facebook BannerAds ===============//

    public void Fb_BannerAd(ViewGroup ad_frameLayout) {

        CommonData.LogStartBanner();
        String logMsg = CommonData.ads_Priority + " -> Fb_BannerAd() -> ";
        CommonData.LogBanner(logMsg + "Start");

        CommonData.facebook_bannerAd = new com.facebook.ads.AdView(activity, new AdsPreferences(activity).getFacebookBanner(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        ad_frameLayout.addView(CommonData.facebook_bannerAd);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                CommonData.LogBanner(logMsg + "onError");
                Fb_BannerAd_2(ad_frameLayout);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                CommonData.LogBanner(logMsg + "onAdLoaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
                CommonData.LogBanner(logMsg + "onAdClicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                CommonData.LogBanner(logMsg + "onLoggingImpression");
            }
        };

        CommonData.facebook_bannerAd.loadAd(CommonData.facebook_bannerAd.buildLoadAdConfig().withAdListener(adListener).build());
    }

    public void Fb_BannerAd_2(ViewGroup ad_frameLayout) {

        CommonData.LogStartBanner();
        String logMsg = CommonData.ads_Priority + " -> Fb_BannerAd_2() -> ";
        CommonData.LogBanner(logMsg + "Start");

        CommonData.facebook_bannerAd = new com.facebook.ads.AdView(activity, new AdsPreferences(activity).getFacebookBanner(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        ad_frameLayout.addView(CommonData.facebook_bannerAd);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                CommonData.LogBanner(logMsg + "onError");
                if (CommonData.is_Facebook_Google) {
                    G_Admob_BannerAd(ad_frameLayout);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                CommonData.LogBanner(logMsg + "onAdLoaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
                CommonData.LogBanner(logMsg + "onAdClicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                CommonData.LogBanner(logMsg + "onLoggingImpression");
            }
        };

        CommonData.facebook_bannerAd.loadAd(CommonData.facebook_bannerAd.buildLoadAdConfig().withAdListener(adListener).build());
    }


    //============= Manage BannerAds ===============//
    //============= Manage BannerAds ===============//

    public void Manage_BannerAds(ViewGroup ad_frameLayout) {
        if (!CommonData.checkAppUpdate(activity)) {
            if (new AdsPreferences(activity).getAdsOnFlag()) {
                if (new AdsPreferences(activity).getNativeShowBottom().equalsIgnoreCase("on")) {
                    View view = LayoutInflater.from(activity).inflate(R.layout.shimmer_banner, ad_frameLayout, false);
                    ad_frameLayout.addView(view);
                    if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                        G_Admob_BannerAd(ad_frameLayout);
                    } else if (CommonData.is_Facebook || CommonData.is_Facebook_Google) {
                        Fb_BannerAd(ad_frameLayout);
                    }
                } else if (new AdsPreferences(activity).getNativeShowBottom().equalsIgnoreCase("on2")) {
                    new NativeAdManager(activity).show_NativeSmallAd(ad_frameLayout);
                } else {
                    ad_frameLayout.setVisibility(View.GONE);
                }
            } else {
                ad_frameLayout.setVisibility(View.GONE);
            }
        } else {
            ad_frameLayout.setVisibility(View.GONE);
        }
    }

}
