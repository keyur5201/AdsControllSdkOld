package com.adssdkmanager.loadData;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.IBinder;

import androidx.annotation.NonNull;

import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.TrackingConstants;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.VpnException;

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        Network[] networks = cm.getAllNetworks();
        boolean is_vpn_connect = false;

        for (Network network : networks) {
            NetworkCapabilities caps = cm.getNetworkCapabilities(network);
            if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                is_vpn_connect = true;
            }
        }

        if (new AdsPreferences(this).getServerFlag() && is_vpn_connect) {
            UnifiedSdk.getInstance().getVpn().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                @Override
                public void complete() {
                }

                @Override
                public void error(@NonNull VpnException e) {
                    e.printStackTrace();
                }
            });
        }

        stopSelf();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onTaskRemoved(rootIntent);
    }

}