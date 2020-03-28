package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.dialogs.NotExecutable;

public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkPermission();

    }

    private void moveToNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000L);
    }

    private void checkPermission() {
        //6.0未満なのか？
        if(Build.VERSION_CODES.M > Build.VERSION.SDK_INT) {
            moveToNext();
            return;
        }

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            moveToNext();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(PERMISSION_REQUEST_CODE == requestCode) {
            //権限がない場合のダイアログ結果振り分け
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //権限を許可したからそのままアプリ起動
                moveToNext();
            } else {
                //許可がないと実行できないよの処理
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("アプリを起動するのには、位置情報の許可が必須です。");
                builder.setNegativeButton("アプリを終了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //クリックしたときの処理
                                finish();
                            }
                        });
                builder.show();
            }

        }
    }
}
