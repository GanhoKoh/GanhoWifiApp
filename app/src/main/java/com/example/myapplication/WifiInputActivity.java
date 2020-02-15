package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WifiInputActivity extends AppCompatActivity {

    Button mConnectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_input);

        Intent intent = getIntent();
        String wifiSSID = intent.getStringExtra("WifiSSID");

        TextView textView = findViewById(R.id.wifi_name_txt);
        textView.setText(wifiSSID);

        mConnectBtn = findViewById(R.id.connect_bttn);
        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
