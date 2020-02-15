package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.net.wifi.WifiManager;

public class WifiManagerHolder {

    private static WifiManagerHolder wifiManagerHolder = new WifiManagerHolder();

    private WifiManagerHolder() {}

    public static WifiManagerHolder getInstance() { return wifiManagerHolder; }

    private WifiManager wifiManager;

    private BroadcastReceiver broadcastReceiver;

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    public void setWifiManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    public void setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        this.broadcastReceiver = broadcastReceiver;
    }
}
