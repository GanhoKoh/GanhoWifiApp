package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WifiInputActivity extends AppCompatActivity {

    public static final String KEY_WIFI_DATA = "key_wifi_data";
    Button mConnectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_input);

        Intent intent = getIntent();
        ScanResult scanResult = intent.getParcelableExtra(KEY_WIFI_DATA);


        TextView textView = findViewById(R.id.wifi_name_txt);
        textView.setText(scanResult == null ? "" : scanResult.SSID);

        mConnectBtn = findViewById(R.id.connect_bttn);
        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
