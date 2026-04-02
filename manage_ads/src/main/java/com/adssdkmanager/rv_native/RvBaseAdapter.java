package com.adssdkmanager.rv_native;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adssdkmanager.R;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.loadData.AdsPreferences;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class RvBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;

    public RvBaseAdapter(Activity activity) {
        this.activity = activity;
    }

    public abstract int setItemLayout();

    public abstract void itemBind(@NonNull RecyclerView.ViewHolder holder, int position);

    public abstract ArrayList<Object> arrayList();

    public abstract RecyclerView.ViewHolder viewHolder(View view);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == CommonData.TYPE_AD) {
            if (CommonData.is_Facebook_Rv) {
                View adView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fb_native_big, viewGroup, false);
                return new FacebookAdViewHolder(adView);
            } else {
                View adView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_ad_container, viewGroup, false);
                return new GoogleAdViewHolder(adView);
            }
        } else {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(setItemLayout(), viewGroup, false);
            return viewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FacebookAdViewHolder || holder instanceof GoogleAdViewHolder) {
            if (CommonData.is_Facebook_Rv) {
                try {
                    NativeAd nativeAd = (NativeAd) arrayList().get(position);
                    FacebookAdViewHolder adsViewHolder = (FacebookAdViewHolder) holder;

                    if (!CommonData.checkAppUpdate(activity)) {
                        if (!new AdsPreferences(activity).getAdsOnFlag()) {
                            adsViewHolder.adContainer.setVisibility(View.GONE);
                            adsViewHolder.loadingContainer.setVisibility(View.VISIBLE);
                        } else {
                            adsViewHolder.adContainer.setVisibility(View.GONE);
                            adsViewHolder.loadingContainer.setVisibility(View.GONE);
                        }
                    } else {
                        adsViewHolder.adContainer.setVisibility(View.GONE);
                        adsViewHolder.loadingContainer.setVisibility(View.GONE);
                    }

                    com.facebook.ads.AdOptionsView adOptionsView = new com.facebook.ads.AdOptionsView(holder.itemView.getContext(), nativeAd, adsViewHolder.nativeAdLay);
                    adsViewHolder.adChoicesContainer.removeAllViews();

                    if (adsViewHolder.nativeAdTitle != null) {
                        adsViewHolder.adChoicesContainer.addView(adOptionsView, 0);
                        adsViewHolder.nativeAdTitle.setText(nativeAd.getAdvertiserName());

                        adsViewHolder.adContainer.setVisibility(View.VISIBLE);
                        adsViewHolder.loadingContainer.setVisibility(View.GONE);
                    }

                    adsViewHolder.nativeAdBody.setText(nativeAd.getAdBodyText());
                    adsViewHolder.nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                    adsViewHolder.nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                    adsViewHolder.nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                    adsViewHolder.sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

                    List<View> clickableViews = new ArrayList<>();
                    clickableViews.add(adsViewHolder.nativeAdTitle);
                    clickableViews.add(adsViewHolder.nativeAdCallToAction);

                    if (new AdsPreferences(holder.itemView.getContext()).getAdsOnFlag()) {
                        nativeAd.registerViewForInteraction(adsViewHolder.itemView, adsViewHolder.nativeAdMedia, adsViewHolder.nativeAdIcon, clickableViews);
                    }

                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    GoogleAdViewHolder googleAdViewHolder = (GoogleAdViewHolder) holder;
                    NativeAdView adView = (NativeAdView) arrayList().get(position);
                    ViewGroup adCardView = (ViewGroup) googleAdViewHolder.itemView;

                    if (new AdsPreferences(holder.itemView.getContext()).getAdsOnFlag()) {
                        if (adCardView.getChildCount() > 0) {
                            adCardView.removeAllViews();
                        }
                        if (adView.getParent() != null) {
                            ((ViewGroup) adView.getParent()).removeView(adView);
                        }
                        adCardView.addView(adView);
                    }

                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        } else {
            itemBind(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (CommonData.is_Facebook_Rv) {
            if (arrayList().get(position) instanceof NativeAd) {
                return CommonData.TYPE_AD;
            } else
                return CommonData.TYPE_ITEM;
        } else {
            return (position % CommonData.ItemsPerAdsNormal == 0) ? CommonData.TYPE_AD : CommonData.TYPE_ITEM;
        }
    }

    public static class GoogleAdViewHolder extends RecyclerView.ViewHolder {
        GoogleAdViewHolder(View view) {
            super(view);
        }
    }

    public static class FacebookAdViewHolder extends RecyclerView.ViewHolder {

        public NativeAdLayout nativeAdLay;
        public MediaView nativeAdIcon;
        public TextView nativeAdTitle;
        public MediaView nativeAdMedia;
        public TextView nativeAdSocialContext;
        public TextView nativeAdBody;
        public TextView sponsoredLabel;
        public Button nativeAdCallToAction;
        LinearLayout adChoicesContainer;
        LinearLayout adContainer;
        LinearLayout loadingContainer;

        public FacebookAdViewHolder(@NonNull View itemView) {
            super(itemView);

            nativeAdLay = itemView.findViewById(R.id.nativeAdLay);
            nativeAdIcon = itemView.findViewById(R.id.native_ad_icon);
            nativeAdTitle = itemView.findViewById(R.id.native_ad_title);
            nativeAdMedia = itemView.findViewById(R.id.native_ad_media);
            nativeAdSocialContext = itemView.findViewById(R.id.native_ad_social_context);
            nativeAdBody = itemView.findViewById(R.id.native_ad_body);
            sponsoredLabel = itemView.findViewById(R.id.native_ad_sponsored_label);
            nativeAdCallToAction = itemView.findViewById(R.id.native_ad_call_to_action);

            adChoicesContainer = itemView.findViewById(R.id.ad_choices_container);
            adContainer = itemView.findViewById(R.id.adContainer);
            loadingContainer = itemView.findViewById(R.id.loadingContainer);

        }
    }

    public void initNativeAds() {
        if (CommonData.is_Facebook_Rv) {
            int no_of_ad_request = arrayList().size() / (CommonData.ItemsPerAdsNormal - 1);
            CommonData.nativeAdsManager = new NativeAdsManager(activity, new AdsPreferences(activity).getFacebookNative(), no_of_ad_request);
        }
        CommonData.nativeAdsManager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                int count = CommonData.nativeAdsManager.getUniqueNativeAdCount();
                for (int i = 0; i < count; i++) {
                    NativeAd ad = CommonData.nativeAdsManager.nextNativeAd();
                    addNativeAds(i, ad);
                }
            }

            @Override
            public void onAdError(AdError adError) {
            }
        });
        CommonData.nativeAdsManager.loadAds();
    }

    public void addNativeAds(int i, NativeAd ad) {

        if (ad == null) {
            return;
        }
        if (CommonData.nativeAdList.size() > i && CommonData.nativeAdList.get(i) != null) {
            CommonData.nativeAdList.get(i).unregisterView();
            arrayList().remove(CommonData.Fb_Rv_FirstAd_Position + (i * CommonData.ItemsPerAdsNormal));
            CommonData.nativeAdList = null;
            notifyDataSetChanged();
        }
        CommonData.nativeAdList.add(i, ad);
        if (arrayList().size() > CommonData.Fb_Rv_FirstAd_Position + (i * CommonData.ItemsPerAdsNormal)) {
            arrayList().add(CommonData.Fb_Rv_FirstAd_Position + (i * CommonData.ItemsPerAdsNormal), ad);
            notifyItemInserted(CommonData.Fb_Rv_FirstAd_Position + (i * CommonData.ItemsPerAdsNormal));
        }

    }

}
