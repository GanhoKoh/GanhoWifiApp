package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WifiMeterActivity extends AppCompatActivity {

    WifiInfo mWifiInfo;
    Timer mTimer;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_meter);

        mHandler = new Handler();

        mTimer = new Timer();
        //接続されている前提なので、その情報を取得する

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mWifiInfo = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
                Log.d("mWifiInfo", String.valueOf(mWifiInfo.getRssi()));
            }
        }, 1000, 1000);

    }
}
