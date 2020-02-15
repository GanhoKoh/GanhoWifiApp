package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ScanResultListAdapter extends ArrayAdapter<ScanResult> {

    private LayoutInflater mLayoutInflater;
    private List<ScanResult> mScanResultList;
    private Context mContext;

    public ScanResultListAdapter(@NonNull Context context, List<ScanResult> ScanResultList) {
        super(context, R.layout.wifi_list, ScanResultList);
        mLayoutInflater = LayoutInflater.from(context);
        mScanResultList = ScanResultList;
        mContext = context;
    }

    @Override
    public int getCount() {
       return mScanResultList.size();
    }

    @Nullable
    @Override
    public ScanResult getItem(int position) {
        return mScanResultList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView ssidNameText;
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.wifi_list, parent, false);
            ssidNameText = convertView.findViewById(R.id.wifi_ssid_name);
            convertView.setTag(ssidNameText);
        } else {
            ssidNameText = (TextView) convertView.getTag();
        }

        ScanResult item = mScanResultList.get(position);
        ssidNameText.setText(item.SSID);
        return  convertView;
    }
}
