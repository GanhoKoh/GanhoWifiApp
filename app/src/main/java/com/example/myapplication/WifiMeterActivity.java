package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class WifiMeterActivity extends AppCompatActivity {

    WifiInfo mWifiInfo;
    TextView mSsidName;
    TextView mRssi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_meter);

        //接続されている前提なので、その情報を取得する
        mWifiInfo = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();

        //表示用ビューを用意する
        mSsidName = findViewById(R.id.ssid_name);
        mRssi = findViewById(R.id.rssi_txt);

        mSsidName.setText(mWifiInfo.getSSID());
        mRssi.setText(mWifiInfo.getRssi());
    }
}
