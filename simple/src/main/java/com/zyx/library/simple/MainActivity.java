package com.zyx.library.simple;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.zyx.library.simple.activitys.BinderActivity;
import com.zyx.library.simple.activitys.SecondMainActivity;
import com.zyx.library.simple.bean.ResonseData;
import com.zyx.library.simple.urlconnecttion.HttpUtils;
import com.zyx.library.simple.urlconnecttion.IJsonDataListener;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondMainActivity.class);
                startActivity(intent);
            }
        });
        
        findViewById(R.id.getresponse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        HttpUtils.sendJsonRequest("http://api.2dfire-pre.com/cash-api/check_health", null, ResonseData.class, new IJsonDataListener() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFail() {

            }
        });
    }

    public void jumpBinder(View view) {
        Intent intent = new Intent(MainActivity.this, BinderActivity.class);
        startActivity(intent);
    }
}
