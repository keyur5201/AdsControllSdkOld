package com.adssdkmanager.ads_manager;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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

import jp.wasabeef.glide.transformations.BlurTransformation;


public class OtherAdsManager {

    Activity activity;

    public OtherAdsManager(Activity activity) {
        this.activity = activity;
    }


    public void showQurekaMix(ImageView imageView, String folderName) {
//        if (generateAdsUrl(folderName).endsWith(".gif")) {
            Glide.with(activity).load(generateAdsUrl(folderName)).into(imageView);
//        } else {
//            Glide.with(activity).load(generateAdsUrl(folderName)).into(new DrawableImageViewTarget(imageView));
//        }
    }

    public void showQurekaInterstitial(OnAdCallBack onAdCallBack) {
        if (CommonData.isShowOtherAds(activity)) {
            CommonData.is_ShowingAd = true;
            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.qu_interstitial);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.black));
            dialog.setCancelable(false);
            dialog.show();


            ImageView quInterstitialSmall = dialog.findViewById(R.id.quInterstitialSmall);
            ImageView quInterstitial = dialog.findViewById(R.id.quInterstitial);
            ImageView quIcon = dialog.findViewById(R.id.quIcon);
            TextView quHeader = dialog.findViewById(R.id.quHeader);
            TextView quBody = dialog.findViewById(R.id.quBody);

            String quImage = generateAdsUrl(CommonData.other_fullscreen_image_folder);

            String[] header = activity.getResources().getStringArray(R.array.header);
            String randomheader = header[new Random().nextInt(header.length)];
            String[] body = activity.getResources().getStringArray(R.array.body);
            String randombody = body[new Random().nextInt(body.length)];

            quHeader.setText(randomheader);
            quBody.setText(randombody);

            Glide.with(activity).load(generateAdsUrl(CommonData.other_icon_square_folder)).into(quIcon);

            Glide.with(activity).load(quImage).into(quInterstitialSmall);

            Glide.with(activity).load(quImage)
                    .apply(new RequestOptions().transforms(new BlurTransformation(25)))
                    .into(quInterstitial);

            quInterstitial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CommonAppMethodes(activity).ChromeView(new OtherAdsManager(activity).adsRedirectUrl());
                }
            });

            dialog.findViewById(R.id.closeAd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    CommonData.is_ShowingAd = false;
                    onAdCallBack.onAdDismiss();
                }
            });
        } else {
            onAdCallBack.onAdDismiss();
        }
    }

    public void showQurekaAppopen(OnAdCallBack onAdCallBack) {
        if (CommonData.isShowOtherAds(activity)) {

            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.qu_appopen);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.black));
            dialog.setCancelable(false);
            dialog.show();

            CommonData.is_ShowingAd = true;

            ImageView quInterstitialSmall = dialog.findViewById(R.id.quInterstitialSmall);
            ImageView quInterstitial = dialog.findViewById(R.id.quInterstitial);
            ImageView quIcon = dialog.findViewById(R.id.quIcon);
            TextView quHeader = dialog.findViewById(R.id.quHeader);

            String quImage = generateAdsUrl(CommonData.other_fullscreen_image_folder);

            String[] header = activity.getResources().getStringArray(R.array.header);
            String randomheader = header[new Random().nextInt(header.length)];

            quHeader.setText(randomheader);

            Glide.with(activity).load(generateAdsUrl(CommonData.other_icon_square_folder)).into(quIcon);

            Glide.with(activity).load(quImage).into(quInterstitialSmall);

            Glide.with(activity).load(quImage)
                    .apply(new RequestOptions().transforms(new BlurTransformation(25)))
                    .into(quInterstitial);

            quInterstitial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CommonAppMethodes(activity).ChromeView(new OtherAdsManager(activity).adsRedirectUrl());
                }
            });

            dialog.findViewById(R.id.manager).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    CommonData.is_ShowingAd = false;
                    onAdCallBack.onAdDismiss();
                }
            });
        } else {
            onAdCallBack.onAdDismiss();
        }
    }

    public String generateAdsUrl(String folderName) {
        String url = BuildConfig.QuBaseUrl;
        String[] separated = folderName.split(",");

        String folderSize;
        String folder;

        if (otherAdsPriority().equalsIgnoreCase("Qureka")) {
            folder = "qureka_";
            folderSize = separated[1];
        } else {
            folder = "predchamp_";
            folderSize = separated[2];
        }

        if (separated[0].equalsIgnoreCase("gif")) {
            url = url + otherAdsPriority() + "/" + folder + separated[0] + "/" + randomNumber(Integer.parseInt(folderSize)) + ".gif";
        } else {
            url = url + otherAdsPriority() + "/" + folder + separated[0] + "/" + randomNumber(Integer.parseInt(folderSize)) + ".webp";
        }
        Log.i("kkkkkkkkkk", "generateAdsUrl:0 "  + url);
        return url;
    }

    public String adsRedirectUrl() {
        String[] separated = new AdsPreferences(activity).getOtherAdsUrl().split(",");

        String url;

        if (otherAdsPriority().equalsIgnoreCase("Qureka")) {
            url = separated[0];
        } else {
            url = separated[1];
        }
        return url;
    }

    public String otherAdsPriority() {
        if (new AdsPreferences(activity).getOtherAds().equalsIgnoreCase("q")) {
            return "Qureka";
        } else if (new AdsPreferences(activity).getOtherAds().equalsIgnoreCase("p")) {
            return "PredChamp";
        } else if (new AdsPreferences(activity).getOtherAds().equalsIgnoreCase("qp")) {
            if (randomNumber(2) == 2) {
                return "PredChamp";
            } else {
                return "Qureka";
            }
        } else {
            return "off";
        }
    }

    public void adsClick(View adView) {
        adView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonAppMethodes(activity).ChromeView(adsRedirectUrl());
            }
        });
    }

    public int randomNumber(int max) {
        List<Integer> quMedia = new ArrayList<Integer>();
        for (int i = 1; i <= max; i++) {
            quMedia.add(i);
        }
        return quMedia.get(new Random().nextInt(quMedia.size()));
    }
}
