package com.adssdkmanager.rv_native;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adssdkmanager.R;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.loadData.AdsPreferences;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.Objects;

public class RecycleviewAdManager {

    Activity activity;

    public RecycleviewAdManager(Activity activity) {
        this.activity = activity;
    }


    //=============== Load NativeAd In Recycleview =======================
    //=============== Load NativeAd In Recycleview =======================

    public void ShowNativeAdInRecyclerView(int adSize, ArrayList<Object> arrayList, boolean isShowAdInGrid, AdspterSet adspterSet) {

        if (CommonData.is_Google_Rv) {
            AddNativeAd(adSize, arrayList);
            LoadNativeAd(adSize, arrayList);
        }

        if (isShowAdInGrid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, CommonData.GridCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (CommonData.is_Facebook_Rv) {
                        if (arrayList.get(position) instanceof com.facebook.ads.NativeAd) {
                            return CommonData.GridCount;
                        } else
                            return 1;
                    } else {
                        return (position % CommonData.ItemsPerAdsNormal == 0) ? CommonData.GridCount : 1;
                    }
                }
            });

            adspterSet.onAddDataFinish(gridLayoutManager);
        } else {
            adspterSet.onAddDataFinish(new LinearLayoutManager(activity));
        }


        if (CommonData.is_Facebook_Rv) {
            CommonData.nativeAdList.clear();
            InitFacebookNativeAds(arrayList);
        }
    }

    public void InitFacebookNativeAds(ArrayList<Object> arrayList) {
        if (CommonData.is_Facebook_Rv) {
            int no_of_ad_request = arrayList.size() / (CommonData.ItemsPerAdsNormal - 1);
            CommonData.nativeAdsManager = new NativeAdsManager(activity, new AdsPreferences(activity).getFacebookNative(), no_of_ad_request);
        }
        CommonData.nativeAdsManager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                int count = CommonData.nativeAdsManager.getUniqueNativeAdCount();
                for (int i = 0; i < count; i++) {
                    com.facebook.ads.NativeAd ad = CommonData.nativeAdsManager.nextNativeAd();
                    AddFacebookNativeAds(i, ad, arrayList);
                }
            }

            @Override
            public void onAdError(AdError adError) {
            }
        });
        CommonData.nativeAdsManager.loadAds();
    }

    public void AddFacebookNativeAds(int i, com.facebook.ads.NativeAd ad, ArrayList<Object> arrayList) {
        if (new AdsPreferences(activity).getAppUpdateFlag() && new AdsPreferences(activity).getAppCurrentVersion()
                .equalsIgnoreCase(new AdsPreferences(activity).getDefaultAppVersion())) {
            return;
        }
        if (!new AdsPreferences(activity).getAdsOnFlag()) {
            return;
        }
        if (ad == null) {
            return;
        }
        if (CommonData.nativeAdList.size() > i && CommonData.nativeAdList.get(i) != null) {
            CommonData.nativeAdList.get(i).unregisterView();
            arrayList.remove(CommonData.Fb_Rv_FirstAd_Position + (i * CommonData.ItemsPerAdsNormal));
            CommonData.nativeAdList = null;
//            notifyDataSetChanged();
        }
        CommonData.nativeAdList.add(i, ad);
        if (arrayList.size() > CommonData.Fb_Rv_FirstAd_Position + (i * CommonData.ItemsPerAdsNormal)) {
            arrayList.add(CommonData.Fb_Rv_FirstAd_Position + (i * CommonData.ItemsPerAdsNormal), ad);
//            notifyItemInserted(CommonData.FIRST_ADS_ITEM_POSITION + (i * CommonData.ItemsPerAdsNormal));
        }

    }

    public void AddNativeAd(int adSize, ArrayList<Object> mainList) {
        int adLayout;
        if (adSize == 1) {
            adLayout = R.layout.am_native_big;
        } else if (adSize == 2) {
            adLayout = R.layout.am_native_banner;
        } else if (adSize == 3) {
            adLayout = R.layout.am_native_small;
        } else {
            adLayout = R.layout.am_native_banner;
        }
        for (int i = 0; i <= mainList.size(); i += CommonData.ItemsPerAdsNormal) {
            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(adLayout, null);

            mainList.add(i, adView);

            if (!CommonData.checkAppUpdate(activity)) {
                if (new AdsPreferences(activity).getAdsOnFlag()) {
                    populateLoadingView(adView, false);
                } else {
                    populateAdOff(adView);
                }
            } else {
                populateAdOff(adView);
            }

        }
    }

    public void LoadNativeAd(int adSize, ArrayList<Object> mainList) {
        LoadNativeAdRv(adSize, mainList, 0);
    }

    public void LoadNativeAdRv(int adSize, ArrayList<Object> mainList, final int index) {
        try {
            if (new AdsPreferences(activity).getAppUpdateFlag() && new AdsPreferences(activity).getAppCurrentVersion()
                    .equalsIgnoreCase(new AdsPreferences(activity).getDefaultAppVersion())) {
                return;
            }
            if (!new AdsPreferences(activity).getAdsOnFlag()) {
                return;
            }
            if (index >= mainList.size()) {
                return;
            }

            Object item = mainList.get(index);
            if (!(item instanceof NativeAdView)) {
                throw new ClassCastException("Expected item at index " + index + " to be a banner ad" + " ad.");
            }

            final NativeAdView adView = (NativeAdView) item;

            com.google.android.gms.ads.AdLoader.Builder builder = new com.google.android.gms.ads.AdLoader.Builder(activity, new AdsPreferences(activity).getAdmobNativeBanner());
            builder.forNativeAd(nativeAd -> {
                populateNativeAdSize(adSize, nativeAd, adView);
                LoadNativeAdRv(adSize, mainList, index + CommonData.ItemsPerAdsNormal);
            });
            com.google.android.gms.ads.VideoOptions videoOptions = new com.google.android.gms.ads.VideoOptions.Builder().setStartMuted(true).build();//false
            com.google.android.gms.ads.nativead.NativeAdOptions adOptions = new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
            builder.withNativeAdOptions(adOptions);

            com.google.android.gms.ads.AdLoader adLoader = builder.withAdListener(new com.google.android.gms.ads.AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError errorCode) {
                    com.google.android.gms.ads.AdLoader.Builder builder = new com.google.android.gms.ads.AdLoader.Builder(activity, new AdsPreferences(activity).getAdxNativeBanner());
                    builder.forNativeAd(nativeAd -> {
                        populateNativeAdSize(adSize, nativeAd, adView);
                        LoadNativeAdRv(adSize, mainList, index + CommonData.ItemsPerAdsNormal);
                    });
                    com.google.android.gms.ads.VideoOptions videoOptions = new com.google.android.gms.ads.VideoOptions.Builder().setStartMuted(true).build();//false
                    com.google.android.gms.ads.nativead.NativeAdOptions adOptions = new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
                    builder.withNativeAdOptions(adOptions);

                    com.google.android.gms.ads.AdLoader adLoader = builder.withAdListener(new com.google.android.gms.ads.AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError errorCode) {
                            populateLoadingView(adView, true);
                            LoadNativeAdRv(adSize, mainList, index + CommonData.ItemsPerAdsNormal);
                        }
                    }).build();
                    adLoader.loadAd(new AdRequest.Builder().build());
                }
            }).build();
            adLoader.loadAd(new AdRequest.Builder().build());
        } catch (NullPointerException | ClassCastException e) {
            e.printStackTrace();
        }
    }


    //=============== Populate Ad Size Validation =======================
    //=============== Populate Ad Size Validation =======================

    public void populateNativeAdSize(int adSize, NativeAd nativeAd, NativeAdView adView) {
        if (adSize == 1) {
            populateAmNativeBigAds(nativeAd, adView);
        } else if (adSize == 2) {
            populateAmNativeBannerAds(nativeAd, adView);
        } else if (adSize == 3) {
            populateAmNativeSmallAds(nativeAd, adView);
        } else {
            populateAmNativeBannerAds(nativeAd, adView);
        }
    }


    //=============== Populate NativeAd =======================
    //=============== Populate NativeAd =======================
    public void populateAmNativeBigAds(NativeAd nativeAd, NativeAdView adView) {
            LinearLayout adContainer = adView.findViewById(R.id.adContainer);
            LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);
            com.google.android.gms.ads.nativead.MediaView ad_media = adView.findViewById(R.id.ad_media);
            AppCompatImageView ad_app_icon = adView.findViewById(R.id.ad_app_icon);
            AppCompatTextView ad_adAttribution = adView.findViewById(R.id.ad_adAttribution);
            AppCompatTextView ad_advertiser = adView.findViewById(R.id.ad_advertiser);
            AppCompatTextView ad_headline = adView.findViewById(R.id.ad_headline);
            AppCompatTextView ad_body = adView.findViewById(R.id.ad_body);
            AppCompatButton ad_call_to_action = adView.findViewById(R.id.ad_call_to_action);

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

            Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));


            if (nativeAd.getHeadline() == null) {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

                adContainer.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.GONE);
            }

            if (nativeAd.getBody() == null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }

            if (nativeAd.getCallToAction() == null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                ((AppCompatButton) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

            //   HideAds: Icon
            if (nativeAd.getIcon() == null) {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
            } else {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.VISIBLE);
                ((AppCompatImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            }

            if (nativeAd.getAdvertiser() == null) {
                Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.GONE);
            } else {
                ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            adView.setNativeAd(nativeAd);
            com.google.android.gms.ads.VideoController vc = nativeAd.getMediaContent().getVideoController();
            if (vc.hasVideoContent()) {
                vc.setVideoLifecycleCallbacks(new com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }

    }

    public void populateAmNativeBannerAds(NativeAd nativeAd, NativeAdView adView) {

            LinearLayout adContainer = adView.findViewById(R.id.adContainer);
            LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);
            com.google.android.gms.ads.nativead.MediaView ad_media = adView.findViewById(R.id.ad_media);
            AppCompatImageView ad_app_icon = adView.findViewById(R.id.ad_app_icon);
            AppCompatTextView ad_adAttribution = adView.findViewById(R.id.ad_adAttribution);
            AppCompatTextView ad_advertiser = adView.findViewById(R.id.ad_advertiser);
            AppCompatTextView ad_headline = adView.findViewById(R.id.ad_headline);
            AppCompatTextView ad_body = adView.findViewById(R.id.ad_body);
            AppCompatButton ad_call_to_action = adView.findViewById(R.id.ad_call_to_action);

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

            Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));


            if (nativeAd.getHeadline() == null) {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

                adContainer.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.GONE);
            }

            if (nativeAd.getBody() == null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }

            if (nativeAd.getCallToAction() == null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                ((AppCompatButton) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

            //   HideAds: Icon
            if (nativeAd.getIcon() == null) {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
            } else {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.VISIBLE);
                ((AppCompatImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            }

            if (nativeAd.getAdvertiser() == null) {
                Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.GONE);
            } else {
                ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            adView.setNativeAd(nativeAd);
            com.google.android.gms.ads.VideoController vc = nativeAd.getMediaContent().getVideoController();
            if (vc.hasVideoContent()) {
                vc.setVideoLifecycleCallbacks(new com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }

    }

    public void populateAmNativeSmallAds(NativeAd nativeAd, NativeAdView adView) {

            LinearLayout adContainer = adView.findViewById(R.id.adContainer);
            LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);
            AppCompatImageView ad_app_icon = adView.findViewById(R.id.ad_app_icon);
            AppCompatTextView ad_adAttribution = adView.findViewById(R.id.ad_adAttribution);
            AppCompatTextView ad_headline = adView.findViewById(R.id.ad_headline);
            AppCompatTextView ad_body = adView.findViewById(R.id.ad_body);
            AppCompatButton ad_call_to_action = adView.findViewById(R.id.ad_call_to_action);

            adView.setHeadlineView(ad_headline);
            adView.setBodyView(ad_body);
            adView.setCallToActionView(ad_call_to_action);
            adView.setIconView(ad_app_icon);

            if (CommonData.isAdsButtonColorFlag(activity)) {
                Drawable tintDr = DrawableCompat.wrap(Objects.requireNonNull(adView.getCallToActionView()).getBackground());
                DrawableCompat.setTint(tintDr, Color.parseColor(new AdsPreferences(activity).getAdsButtonColor()));
                adView.getCallToActionView().setBackground(tintDr);
            }

            if (nativeAd.getHeadline() == null) {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

                adContainer.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.GONE);
            }

            if (nativeAd.getBody() == null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }

            if (nativeAd.getCallToAction() == null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                ((AppCompatButton) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

            //   HideAds: Icon
            if (nativeAd.getIcon() == null) {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
            } else {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.VISIBLE);
                ((AppCompatImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            }

            adView.setNativeAd(nativeAd);
            com.google.android.gms.ads.VideoController vc = nativeAd.getMediaContent().getVideoController();
            if (vc.hasVideoContent()) {
                vc.setVideoLifecycleCallbacks(new com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }

    }


    //=============== Populate Loading View =======================
    //=============== Populate Loading View =======================


    public void populateLoadingView(NativeAdView adView, boolean showLoading) {
        LinearLayout adContainer = adView.findViewById(R.id.adContainer);
        LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);
        TextView ad_headline = adView.findViewById(R.id.ad_headline);

        if (showLoading) {
            adContainer.setVisibility(View.GONE);
            loadingContainer.setVisibility(View.VISIBLE);
        } else {
            if (ad_headline.getText().toString().isEmpty()) {
                adContainer.setVisibility(View.GONE);
                loadingContainer.setVisibility(View.VISIBLE);
            } else {
                adContainer.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.GONE);
            }
        }
    }

    public void populateAdOff(NativeAdView adView) {
        LinearLayout adContainer = adView.findViewById(R.id.adContainer);
        LinearLayout loadingContainer = adView.findViewById(R.id.loadingContainer);
        adContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
    }

}
