package com.example.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.WifiMeterActivity;

import java.util.List;

public class InputDialog extends DialogFragment {

    WifiManager mWifiManager;
    String mSsidName = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();

        final View inputDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.input_dialog, null);

        final TextView textView = inputDialogView.findViewById(R.id.wifi_ssid_name);

        if(args != null) {
            mSsidName = args.getString("ssid");
        } else {
            Log.w("ssid", "ssid not found");
        }
        textView.setText(mSsidName);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inputDialogView);
        builder.setPositiveButton("接続する", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 接続してWifiMaterへ

                EditText editText = inputDialogView.findViewById(R.id.ssid_password);
                String ssidPassword = editText.getText().toString();

                boolean isConnect = connectWifi(mSsidName, ssidPassword);

                if(isConnect) {
                    //WifiMaterへいく
                    Intent intent = new Intent(getActivity(), WifiMeterActivity.class);
                    startActivity(intent);
                } else {
                    //logをだす
                    Log.w("faild connect", "connection faild");
                }
            }
        });
        return builder.create();
    }

    private boolean connectWifi(String ssid, String password) {
        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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

        if (netId != -1) {
            mWifiManager.saveConfiguration();
            mWifiManager.updateNetwork(wc);
            mWifiManager.enableNetwork(netId, true);
            return true;
        }
        return false;
    }
}
