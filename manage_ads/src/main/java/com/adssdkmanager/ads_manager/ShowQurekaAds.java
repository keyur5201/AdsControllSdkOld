package com.adssdkmanager.ads_manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.adssdkmanager.R;
import com.adssdkmanager.common.CommonAppMethodes;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.loadData.AdsPreferences;
import com.adssdkmanager.utils.RoundedImageView;
import com.google.android.material.imageview.ShapeableImageView;

public class ShowQurekaAds extends androidx.appcompat.widget.AppCompatImageView {

    public ShowQurekaAds(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ShowOtherAds);

        int adsType = ta.getInt(R.styleable.ShowOtherAds_ads_folder, 1);

        setAdjustViewBounds(true);

        if (!CommonData.checkAppUpdate((Activity) context)) {
            if (CommonData.isShowOtherAds((Activity) context)) {
                if (adsType == 2) {
                    new OtherAdsManager((Activity) context).showQurekaMix(this, CommonData.other_bottom_banner_folder);
                } else if (adsType == 3) {
                    new OtherAdsManager((Activity) context).showQurekaMix(this, CommonData.other_fullscreen_image_folder);
                } else if (adsType == 4) {
                    new OtherAdsManager((Activity) context).showQurekaMix(this, CommonData.other_gif_folder);
                } else if (adsType == 5) {
                    new OtherAdsManager((Activity) context).showQurekaMix(this, CommonData.other_icon_round_folder);
                } else if (adsType == 6) {
                    new OtherAdsManager((Activity) context).showQurekaMix(this, CommonData.other_icon_square_folder);
                } else if (adsType == 7) {
                    new OtherAdsManager((Activity) context).showQurekaMix(this, CommonData.other_media_image_folder);
                }
            } else {
                setVisibility(GONE);
            }
        } else {
            setVisibility(GONE);
        }

        new OtherAdsManager((Activity) context).adsClick(this);
    }

}
