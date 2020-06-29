package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dialogs.InputDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager mWifiManager;
    private ListView mListView;
    private ScanResultListAdapter mAdapter;
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
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.wifi_list);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //TODO ScanResultListAdapterを作っちゃう
        mAdapter = new ScanResultListAdapter(this, new ArrayList<ScanResult>());

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputDialog inputDialog = new InputDialog();
                inputDialog.show(getSupportFragmentManager(), "inputDialog");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);

        boolean success = mWifiManager.startScan();
        if(success) {
            scanSuccess();
        } else {
            scanFailure();
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mBroadcastReceiver);
        super.onPause();

    }

    private void scanSuccess() {
        Log.d("スキャン成功","スキャン成功");
        List<ScanResult> preResults = mWifiManager.getScanResults();
        List<String> resultsSsidList = new ArrayList<String>();
        List<ScanResult> results = new ArrayList<ScanResult>();

        //SSIDが空のScanresultを削除する
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

        for(ScanResult result : results) {
            Log.d("result.ssid", result.SSID);
        }

        mAdapter.clear();
        mAdapter.addAll(results);
        mAdapter.notifyDataSetChanged();

    }

    private void  scanFailure() {
        Log.d(mWifiManager.getClass().getSimpleName(), "失敗");
    }
}