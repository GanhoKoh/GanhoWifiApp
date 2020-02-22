package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class WifiInputActivity extends AppCompatActivity {

    public static final String KEY_WIFI_DATA = "key_wifi_data";
    Button mConnectBtn;
    TextView mTextView;
    WifiManager mWifiManager;
    ScanResult mScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_input);

        Intent intent = getIntent();
        mScanResult = intent.getParcelableExtra(KEY_WIFI_DATA);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mTextView = findViewById(R.id.wifi_name_txt);
        mTextView.setText(mScanResult != null ? mScanResult.SSID : "");

        mConnectBtn = findViewById(R.id.connect_bttn);
        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText wifi_ssid_pass = findViewById(R.id.SSID_passwrd);
                String inputStr = wifi_ssid_pass.getText().toString();
                Toast toast;

                //入力されていなければその旨を伝える。そしてreturn;
                if(inputStr.equals("")) {
                    toast = Toast.makeText(WifiInputActivity.this, "入力されていません", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                //コネクトを試みる
                if(connectWifi(mScanResult.SSID, inputStr)) {
                    //コネクトニ成功したらWifiの強度表示画面へ
                    toast = Toast.makeText(WifiInputActivity.this, "接続に成功しました", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(WifiInputActivity.this, WifiMeterActivity.class);
                    startActivity(intent);

                } else {
                    toast = Toast.makeText(WifiInputActivity.this, "接続に失敗しました", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
            }
        });
    }

    private boolean connectWifi(String ssid, String password) {
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + ssid + "\"";
        wc.status = WifiConfiguration.Status.ENABLED;

        wc.preSharedKey = "\"" + password + "\"";
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

        List<WifiConfiguration> networkList = mWifiManager.getConfiguredNetworks();
        if (networkList != null) {
            for (WifiConfiguration item : networkList) {
                // 事前にアクセスポイントの無効化を解除する
                // アクセスポイントが無効化されたままだとOSバージョンによって接続できない
                mWifiManager.enableNetwork(item.networkId, false);
            }
        }

        int netId = mWifiManager.addNetwork(wc);

        if(netId != -1) {
            mWifiManager.saveConfiguration();
            mWifiManager.updateNetwork(wc);
            mWifiManager.enableNetwork(netId, true);
            return true;
        }
        return  false;
    }
}
