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
                Intent intent = new Intent(MainActivity.this, WifiInputActivity.class);
                //TODOデータ受けたし
                ScanResult item = mAdapter.getItem(position);
                intent.putExtra(WifiInputActivity.KEY_WIFI_DATA, item);
                startActivity(intent);
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
            WifiManagerHolder wifiManagerHolder = WifiManagerHolder.getInstance();
            wifiManagerHolder.setWifiManager(mWifiManager);
            wifiManagerHolder.setBroadcastReceiver(mBroadcastReceiver);
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
        List<ScanResult> results = mWifiManager.getScanResults();

        mAdapter.clear();
        mAdapter.addAll(results);
        mAdapter.notifyDataSetChanged();

    }

    private void  scanFailure() {
        Log.d(mWifiManager.getClass().getSimpleName(), "失敗");
    }

}
