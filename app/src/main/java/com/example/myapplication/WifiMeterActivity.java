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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ntt.customgaugeview.library.GaugeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class WifiMeterActivity extends AppCompatActivity {

    private static final int MIN_RSSI = -100;
    private static final int MAX_RSSI = 0;

    private GaugeView mGaugeView;
    private Button mReturnBtn;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_meter);
        this.mGaugeView = findViewById(R.id.gauge_view);
        mGaugeView.setShowRangeValues(true);
        mGaugeView.setTargetValue(0);

        mReturnBtn = findViewById(R.id.return_btn);
        mReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                showWifiMeter();
            }
        }, 0, 1000);
    }

    @Override
    protected void onPause() {
        mTimer.cancel();
        mTimer = null;
        super.onPause();
    }

    private int calculateSignalLevel(int rssi) {
        if(MIN_RSSI > rssi) {
            return 0;
        } else if( MAX_RSSI < rssi) {
            return 100;
        } else {
            return rssi + 100;
        }
    }

    private void showWifiMeter() {
        final WifiInfo wifiInfo = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final float level = calculateSignalLevel(wifiInfo.getRssi());
                mGaugeView.setTargetValue(level);
            }
        });
    }
}