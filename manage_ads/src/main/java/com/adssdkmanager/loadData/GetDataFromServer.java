package com.adssdkmanager.loadData;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.adssdkmanager.ads_manager.AppOpenManager;
import com.adssdkmanager.ads_manager.InterstitialAdManager;
import com.adssdkmanager.ads_manager.NativeAdManager;
import com.adssdkmanager.common.CommonData;
import com.adssdkmanager.common.OnAdCallBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import unified.vpn.sdk.AFVpnService;
import unified.vpn.sdk.AuthMethod;
import unified.vpn.sdk.Callback;
import unified.vpn.sdk.ClientInfo;
import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.HydraTransport;
import unified.vpn.sdk.HydraTransportConfig;
import unified.vpn.sdk.OpenVpnTransport;
import unified.vpn.sdk.OpenVpnTransportConfig;
import unified.vpn.sdk.SessionConfig;
import unified.vpn.sdk.TrackingConstants;
import unified.vpn.sdk.TransportConfig;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.User;
import unified.vpn.sdk.VpnException;
import unified.vpn.sdk.VpnPermissions;
import unified.vpn.sdk.VpnState;

public class GetDataFromServer {

    Activity activity;


    public GetDataFromServer(Activity activity) {
        this.activity = activity;
    }


    //================== Load Data From Server - Start ======================//
    //================== Load Data From Server - Start ======================//


    public void loadAdsData(String packageName, Class<?> onStablePosition, Class<?> onUpdatePosition) {

        getDataUsingFirebase(packageName, new OnAdCallBack() {
            @Override
            public void onAdDismiss() {
                activity.startActivity(new Intent(activity, onStablePosition));
            }
        }, new OnAdCallBack() {
            @Override
            public void onAdDismiss() {
                activity.startActivity(new Intent(activity, onUpdatePosition));
            }
        });

    }


    public void getDataUsingFirebase(String packageName, OnAdCallBack onStablePosition, OnAdCallBack onUpdatePosition) {
        AdsPreferences adsPreferences = new AdsPreferences(activity);

        PackageManager pm = activity.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pkgInfo.versionName;

        String pName = packageName.replaceAll("\\.", "_");

        DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference().child("v2_" + pName);
        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CommonData.isAppopenAvailableToShow = false;
                adsPreferences.setDefaultAppVersion(versionName);

                adsPreferences.setAppUpdateFlag(Boolean.TRUE.equals(snapshot.child(AdsPreferences.appUpdateFlag).getValue(Boolean.class)));
                adsPreferences.setAppCurrentVersion(snapshot.child(AdsPreferences.appCurrentVersion).getValue(String.class));
                adsPreferences.setAdsOnFlag(Boolean.TRUE.equals(snapshot.child(AdsPreferences.adsOnFlag).getValue(Boolean.class)));
                adsPreferences.setNativePreload(Boolean.TRUE.equals(snapshot.child(AdsPreferences.nativePreload).getValue(Boolean.class)));
                adsPreferences.setInterstitialPreload(Boolean.TRUE.equals(snapshot.child(AdsPreferences.interstitialPreload).getValue(Boolean.class)));
                adsPreferences.setAdsPriority(snapshot.child(AdsPreferences.adsPriority).getValue(String.class));
                adsPreferences.setSplashInterstitial(snapshot.child(AdsPreferences.splashInterstitial).getValue(String.class));
                adsPreferences.setShowGfInterstitial(Boolean.TRUE.equals(snapshot.child(AdsPreferences.showGfInterstitial).getValue(Boolean.class)));
                adsPreferences.setAdsClickGoogle(snapshot.child(AdsPreferences.adsClickGoogle).getValue(Integer.class));
                adsPreferences.setAdsClickFacebook(snapshot.child(AdsPreferences.adsClickFacebook).getValue(Integer.class));
                adsPreferences.setShowNativeLoader(Boolean.TRUE.equals(snapshot.child(AdsPreferences.showNativeLoader).getValue(Boolean.class)));
                adsPreferences.setAdsBackFlag(Boolean.TRUE.equals(snapshot.child(AdsPreferences.adsBackFlag).getValue(Boolean.class)));
                adsPreferences.setNativeShowBottom(snapshot.child(AdsPreferences.nativeShowBottom).getValue(String.class));
                adsPreferences.setAdsRecyclerPosition(snapshot.child(AdsPreferences.adsRecyclerPosition).getValue(Integer.class));
                adsPreferences.setOtherAds(snapshot.child(AdsPreferences.otherAds).getValue(String.class));
                adsPreferences.setOtherAdsUrl(snapshot.child(AdsPreferences.otherAdsUrl).getValue(String.class));
                adsPreferences.setAdsButtonColor(snapshot.child(AdsPreferences.adsButtonColor).getValue(String.class));
                adsPreferences.setPrivacyUrl(snapshot.child(AdsPreferences.privacyUrl).getValue(String.class));
                adsPreferences.setOneSignalId(snapshot.child(AdsPreferences.oneSignalId).getValue(String.class));

                adsPreferences.setAdmobAppOpen(snapshot.child(AdsPreferences.admobAppOpen).getValue(String.class));
                adsPreferences.setAdmobBanner(snapshot.child(AdsPreferences.admobBanner).getValue(String.class));
                adsPreferences.setAdmobInterstitial(snapshot.child(AdsPreferences.admobInterstitial).getValue(String.class));
                adsPreferences.setAdmobNative(snapshot.child(AdsPreferences.admobNative).getValue(String.class));
                adsPreferences.setAdmobNativeBanner(snapshot.child(AdsPreferences.admobNativeBanner).getValue(String.class));
                adsPreferences.setAdxAppOpen(snapshot.child(AdsPreferences.adxAppOpen).getValue(String.class));
                adsPreferences.setAdxBanner(snapshot.child(AdsPreferences.adxBanner).getValue(String.class));
                adsPreferences.setAdxInterstitial(snapshot.child(AdsPreferences.adxInterstitial).getValue(String.class));
                adsPreferences.setAdxNative(snapshot.child(AdsPreferences.adxNative).getValue(String.class));
                adsPreferences.setAdxNativeBanner(snapshot.child(AdsPreferences.adxNativeBanner).getValue(String.class));
                adsPreferences.setFacebookBanner(snapshot.child(AdsPreferences.facebookBanner).getValue(String.class));
                adsPreferences.setFacebookInterstitial(snapshot.child(AdsPreferences.facebookInterstitial).getValue(String.class));
                adsPreferences.setFacebookNative(snapshot.child(AdsPreferences.facebookNative).getValue(String.class));
                adsPreferences.setFacebookNativeBanner(snapshot.child(AdsPreferences.facebookNativeBanner).getValue(String.class));

                adsPreferences.setServerFlag(Boolean.TRUE.equals(snapshot.child(AdsPreferences.serverFlag).getValue(Boolean.class)));
                adsPreferences.setServerBaseUrl(snapshot.child(AdsPreferences.serverBaseUrl).getValue(String.class));
                adsPreferences.setServerCarrierid(snapshot.child(AdsPreferences.serverCarrierid).getValue(String.class));
                adsPreferences.setServerCountry(snapshot.child(AdsPreferences.serverCountry).getValue(String.class));
                adsPreferences.setServerConnectCountryList(snapshot.child(AdsPreferences.serverConnectCountryList).getValue(String.class));
                adsPreferences.setConnectServerOnSplash(Boolean.TRUE.equals(snapshot.child(AdsPreferences.connectServerOnSplash).getValue(Boolean.class)));

                CommonData.setPriorityClickData(activity);

                if (CommonData.checkAppUpdate(activity)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onUpdatePosition.onAdDismiss();
                        }
                    }, 3000);
                } else {
                    if (adsPreferences.getServerFlag() && adsPreferences.getConnectServerOnSplash()) {
                        initHydraSdk(onStablePosition);
                    } else {
                        adsLoadMethode(onStablePosition);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void adsLoadMethode(OnAdCallBack onStablePosition) {

        if (new AdsPreferences(activity).getAdsOnFlag()) {

            if (new AdsPreferences(activity).getNativePreload()) {
                if (CommonData.is_Google || CommonData.is_Google_Facebook) {

                    new NativeAdManager(activity).admob_NativeAd_load(null);
                    new NativeAdManager(activity).admob_NativeBannerAd_load(null);
                    new NativeAdManager(activity).admob_NativeSmallAd_load(null);

                } else if (CommonData.is_Facebook || CommonData.is_Facebook_Google) {

                    new NativeAdManager(activity).fb_NativeAd_load(null);
                    new NativeAdManager(activity).fb_NativeBannerAd_load(null);
                    new NativeAdManager(activity).fb_NativeSmallAd_load(null);

                }
            }

            if (new AdsPreferences(activity).getInterstitialPreload()) {
                if (CommonData.is_Google || CommonData.is_Google_Facebook) {
                    new InterstitialAdManager(activity).admob_Interstitial_load(new OnAdCallBack() {
                        @Override
                        public void onAdDismiss() {

                        }
                    });
                } else if (CommonData.is_Facebook || CommonData.is_Facebook_Google) {
                    new InterstitialAdManager(activity).fb_Interstitial_load(new OnAdCallBack() {
                        @Override
                        public void onAdDismiss() {

                        }
                    });
                }
            }

            new AppOpenManager(activity).Google_Admob_Appopen(new OnAdCallBack() {
                @Override
                public void onAdDismiss() {
                    if (new AdsPreferences(activity).getSplashInterstitial().equalsIgnoreCase("f")) {
                        new InterstitialAdManager(activity).fb_Interstitial_Compulsory(new OnAdCallBack() {
                            @Override
                            public void onAdDismiss() {
                                CommonData.isAppopenAvailableToShow = true;
                                onStablePosition.onAdDismiss();
                            }
                        });
                    } else if (new AdsPreferences(activity).getSplashInterstitial().equalsIgnoreCase("g")) {
                        new InterstitialAdManager(activity).admob_Interstitial_Compulsory(new OnAdCallBack() {
                            @Override
                            public void onAdDismiss() {
                                CommonData.isAppopenAvailableToShow = true;
                                onStablePosition.onAdDismiss();
                            }
                        });
                    } else {
                        CommonData.isAppopenAvailableToShow = true;
                        onStablePosition.onAdDismiss();
                    }
                }
            });

        } else {
            onStablePosition.onAdDismiss();
        }

    }

    public void initHydraSdk(OnAdCallBack onStablePosition) {

        if (CommonData.checkAppUpdate(activity)) {
            return;
        }

        if (!new AdsPreferences(activity).getServerFlag()) {
            return;
        }

        String[] gstcc = new AdsPreferences(activity).getServerConnectCountryList().split(",");
        for (int i = 0; i < gstcc.length; i++) {
            gstcc[i] = gstcc[i].replaceAll("[^\\w]", "");
            if (gstcc[i].equalsIgnoreCase(country_name()) || gstcc[i].equalsIgnoreCase("all")) {

                List<TransportConfig> transportConfigList = new ArrayList<>();
                transportConfigList.add(HydraTransportConfig.create());
                transportConfigList.add(OpenVpnTransportConfig.tcp());
                transportConfigList.add(OpenVpnTransportConfig.udp());
                UnifiedSdk.update(transportConfigList, CompletableCallback.EMPTY);
                UnifiedSdk.setLoggingLevel(Log.VERBOSE);

                ClientInfo build = ClientInfo.newBuilder().addUrl(new AdsPreferences(activity).getServerBaseUrl()).carrierId(new AdsPreferences(activity).getServerCarrierid()).build();
                UnifiedSdk.getInstance(build);
                PackageManager pm = activity.getPackageManager();
                pm.setComponentEnabledSetting(new ComponentName(activity, AFVpnService.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                UnifiedSdk.getInstance().getBackend().login(AuthMethod.anonymous(), new Callback<User>() {
                    public void success(User user) {
                        UnifiedSdk.getVpnState(new Callback<VpnState>() {

                            @Override
                            public void success(@NonNull VpnState vpnState) {
                                if (vpnState != VpnState.CONNECTED) {
                                    connectToVpn(onStablePosition);
                                }
                            }

                            @Override
                            public void failure(@NonNull VpnException e) {
                                if (new AdsPreferences(activity).getConnectServerOnSplash()) {
                                    adsLoadMethode(onStablePosition);
                                }
                            }
                        });

                    }

                    @Override
                    public void failure(VpnException vpnException) {
                        vpnException.printStackTrace();
                        if (new AdsPreferences(activity).getConnectServerOnSplash()) {
                            adsLoadMethode(onStablePosition);
                        }
                    }
                });
            } else {
                adsLoadMethode(onStablePosition);
            }
        }

    }

    private String country_name() {
        final TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        final String simCountry = tm.getSimCountryIso();
        if (simCountry != null && simCountry.length() == 2) {
            return simCountry.toLowerCase(Locale.US);
        } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
            String networkCountry = tm.getNetworkCountryIso();
            if (networkCountry != null && networkCountry.length() == 2) {
                return networkCountry.toLowerCase(Locale.US);
            }
        }
        return null;
    }

    private void connectToVpn(OnAdCallBack onStablePosition) {

        String[] gscc = new AdsPreferences(activity).getServerCountry().split(",");
        for (int i = 0; i < gscc.length; i++) {
            gscc[i] = gscc[i].replaceAll("[^\\w]", "");
        }

        List<String> fallbackOrder = new ArrayList<>();
        fallbackOrder.add(HydraTransport.TRANSPORT_ID);
        fallbackOrder.add(OpenVpnTransport.TRANSPORT_ID_TCP);
        fallbackOrder.add(OpenVpnTransport.TRANSPORT_ID_UDP);

        SessionConfig sessionConfig = new SessionConfig.Builder().withReason(TrackingConstants.GprReasons.M_UI).withTransportFallback(fallbackOrder)
                .withTransport(HydraTransport.TRANSPORT_ID).withVirtualLocation(gscc[new Random().nextInt(gscc.length)]).build();

        VpnPermissions.request(new CompletableCallback() {
            @Override
            public void complete() {
                UnifiedSdk.getInstance().getVpn().start(sessionConfig, new CompletableCallback() {
                    @Override
                    public void complete() {
                        activity.startService(new Intent(activity, OnClearFromRecentService.class));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (new AdsPreferences(activity).getConnectServerOnSplash()) {
                                    adsLoadMethode(onStablePosition);
                                }
                            }
                        }, 1000);
                    }

                    @Override
                    public void error(@NonNull VpnException e) {
                        if (new AdsPreferences(activity).getConnectServerOnSplash()) {
                            adsLoadMethode(onStablePosition);
                        }
                    }
                });
            }

            @Override
            public void error(@NonNull VpnException e) {
                if (new AdsPreferences(activity).getConnectServerOnSplash()) {
                    initHydraSdk(onStablePosition);
                }
            }

        });

    }

    public void showServerPermissionDialog(OnAdCallBack onStablePosition) {

        if (CommonData.checkAppUpdate(activity)) {
            return;
        }

        if (!new AdsPreferences(activity).getServerFlag()) {
            return;
        }

        VpnPermissions.request(new CompletableCallback() {
            @Override
            public void complete() {
                onStablePosition.onAdDismiss();
            }

            @Override
            public void error(@NonNull VpnException e) {

            }

        });

    }


    //===========================================================
    //===========================================================

    public void insertDataFirebase(String packageName) {

        HashMap<String, Object> adsData = new HashMap<>();

        adsData.put(AdsPreferences.appUpdateFlag, false);
        adsData.put(AdsPreferences.appCurrentVersion, "1.0");
        adsData.put(AdsPreferences.adsOnFlag, true);
        adsData.put(AdsPreferences.nativePreload, false);
        adsData.put(AdsPreferences.interstitialPreload, false);
        adsData.put(AdsPreferences.adsPriority, "g");
        adsData.put(AdsPreferences.splashInterstitial, "off");
        adsData.put(AdsPreferences.showGfInterstitial, false);
        adsData.put(AdsPreferences.adsClickGoogle, 2);
        adsData.put(AdsPreferences.adsClickFacebook, 2);
        adsData.put(AdsPreferences.showNativeLoader, true);
        adsData.put(AdsPreferences.adsBackFlag, true);
        adsData.put(AdsPreferences.nativeShowBottom, "on");
        adsData.put(AdsPreferences.adsRecyclerPosition, 7);
        adsData.put(AdsPreferences.otherAds, "off");
        adsData.put(AdsPreferences.otherAdsUrl, "http://1165.set.qureka.com/");
        adsData.put(AdsPreferences.adsButtonColor, "off");
        adsData.put(AdsPreferences.privacyUrl, "https://www.google.com/");
        adsData.put(AdsPreferences.oneSignalId, "");
        adsData.put(AdsPreferences.admobAppOpen, "ca-app-pub-3940256099942544/3419835294");
        adsData.put(AdsPreferences.admobBanner, "ca-app-pub-3940256099942544/6300978111");
        adsData.put(AdsPreferences.admobInterstitial, "ca-app-pub-3940256099942544/1033173712");
        adsData.put(AdsPreferences.admobNative, "ca-app-pub-3940256099942544/2247696110");
        adsData.put(AdsPreferences.admobNativeBanner, "ca-app-pub-3940256099942544/2247696110");
        adsData.put(AdsPreferences.adxAppOpen, "/6499/example/app-open");
        adsData.put(AdsPreferences.adxBanner, "/6499/example/banner");
        adsData.put(AdsPreferences.adxInterstitial, "/6499/example/interstitial");
        adsData.put(AdsPreferences.adxNative, "/6499/example/native");
        adsData.put(AdsPreferences.adxNativeBanner, "/6499/example/native");
        adsData.put(AdsPreferences.facebookBanner, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        adsData.put(AdsPreferences.facebookInterstitial, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        adsData.put(AdsPreferences.facebookNative, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        adsData.put(AdsPreferences.facebookNativeBanner, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        adsData.put(AdsPreferences.serverFlag, false);
        adsData.put(AdsPreferences.serverBaseUrl, "https://d2isj403unfbyl.cloudfront.net/");
        adsData.put(AdsPreferences.serverCarrierid, "0313_vpn007");
        adsData.put(AdsPreferences.serverCountry, "ca,us");
        adsData.put(AdsPreferences.serverConnectCountryList, "all");
        adsData.put(AdsPreferences.connectServerOnSplash, false);

        packageName = packageName.replaceAll("\\.", "_");

        DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference().child("v2_" + packageName);

        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksRef.setValue(adsData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


}