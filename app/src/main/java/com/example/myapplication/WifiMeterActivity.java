package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WifiMeterActivity extends AppCompatActivity {

    private static final int MIN_RSSI = -100;
    private static final int MAX_RSSI = 0;

    private WifiInfo mWifiInfo;
    private WifiManager mWifiManager;
    private Timer mTimer;
    private Handler mHandler;
    private List<ScanResult> results = new ArrayList<ScanResult>();
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if(success) {
                scanSuccess();
            } else {
                scanFailure();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_meter);

        mHandler = new Handler();


        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);

        boolean success = mWifiManager.startScan();
        if(success) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    scanSuccess();
                }
            }, 1000, 1000);
        } else {
            scanFailure();
        }

    }

    @Override
    protected void onPause() {
        unregisterReceiver(mBroadcastReceiver);
        mTimer.cancel();
        mTimer = null;
        super.onPause();
    }

    private void scanSuccess() {
        List<ScanResult> preResults = mWifiManager.getScanResults();
        List<String> resultsSsidList = new ArrayList<String>();

        for(int i = 0; i < preResults.size(); i++) {
            if(preResults.get(i).SSID.isEmpty()) {
                preResults.remove(i);
            }
        }

        for(ScanResult preResult : preResults) {
            ScanResult targetResult = preResult;
            if(resultsSsidList.contains(targetResult.SSID)) continue;
            for(ScanResult _preResult : preResults) {
                if(preResult.SSID.equals(_preResult.SSID)) {
                    if(targetResult.timestamp < _preResult.timestamp) {
                        targetResult = _preResult;
                    }
                }
            }
            results.add(targetResult);
            resultsSsidList.add(targetResult.SSID);
        }

        mWifiInfo = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        int level = calculateSignalLevel(mWifiInfo.getRssi());
        Log.d("mWifiInfo.変換前", String.valueOf(mWifiInfo.getRssi()));
        Log.d("mWifiInfo.返還後", String.valueOf(level));
    }

    private void scanFailure() {
        Log.d(mWifiManager.getClass().getSimpleName(), "失敗");
    }

    private int calculateSignalLevel(int rssi) {
        if(MIN_RSSI > rssi) {
            return MIN_RSSI;
        } else if( MAX_RSSI < rssi) {
            return MAX_RSSI;
        } else {
            return rssi + 100;
        }
    }
}
