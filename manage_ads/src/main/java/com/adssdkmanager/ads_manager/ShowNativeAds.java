package com.adssdkmanager.ads_manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.adssdkmanager.R;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.loadData.AdsPreferences;

public class ShowNativeAds extends CardView {

    public ShowNativeAds(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ShowNativeAds);

        int adsType = ta.getInt(R.styleable.ShowNativeAds_ads_type, 1);
        String adsCompulsory = CommonData.checkAdsCompulsoryPriority(ta.getInt(R.styleable.ShowNativeAds_ads_compulsory, 1));

        if (!CommonData.checkAppUpdate((Activity) context)) {
            if (new AdsPreferences(context).getAdsOnFlag()) {
                if (adsType == 4) {
                    setCardElevation(0);
                    setRadius(0);
                    setContentPadding(0, 8, 0, 8);
                }
                setCardBackgroundColor(getResources().getColor(R.color.ads_bg_color));
                if (adsCompulsory.equalsIgnoreCase("google")) {
                    if (adsType == 1) {
                        new NativeAdCompulsoryManager((Activity) context).show_NativeAd("google", this);
                    } else if (adsType == 2) {
                        new NativeAdCompulsoryManager((Activity) context).show_NativeBannerAd("google", this);
                    } else if (adsType == 3) {
                        new NativeAdCompulsoryManager((Activity) context).show_NativeSmallAd("google", this);
                    } else if (adsType == 4) {
                        new NativeAdCompulsoryManager((Activity) context).show_NativeSmallAd("google", this);
                    }
                } else if (adsCompulsory.equalsIgnoreCase("facebook")) {
                    if (adsType == 1) {
                        new NativeAdCompulsoryManager((Activity) context).show_NativeAd("facebook", this);
                    } else if (adsType == 2) {
                        new NativeAdCompulsoryManager((Activity) context).show_NativeBannerAd("facebook", this);
                    } else if (adsType == 3) {
                        new NativeAdCompulsoryManager((Activity) context).show_NativeSmallAd("facebook", this);
                    } else if (adsType == 4) {
                        new NativeAdCompulsoryManager((Activity) context).show_NativeSmallAd("facebook", this);
                    }
                } else {
                    if (adsType == 1) {
                        new NativeAdManager((Activity) context).show_NativeAd(this);
                    } else if (adsType == 2) {
                        new NativeAdManager((Activity) context).show_NativeBannerAd(this);
                    } else if (adsType == 3) {
                        new NativeAdManager((Activity) context).show_NativeSmallAd(this);
                    } else if (adsType == 4) {
                        new BannerAdsLoad((Activity) context).Manage_BannerAds(this);
                    }
                }
            }
        }
    }

}
