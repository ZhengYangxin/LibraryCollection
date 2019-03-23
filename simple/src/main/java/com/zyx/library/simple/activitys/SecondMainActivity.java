package com.zyx.library.simple.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.zyx.library.hotfixlibrary.HotFixUtils;
import com.zyx.library.hotfixlibrary.utils.Constants;
import com.zyx.library.hotfixlibrary.utils.FileUtils;
import com.zyx.library.simple.R;
import com.zyx.library.simple.method.ParamSort;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class SecondMainActivity extends AppCompatActivity {
    private static Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_second);
        handler = new MyHandler(SecondMainActivity.this);
        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        findViewById(R.id.fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 补丁文件路径
                        File sourceFile = new File(Environment.getExternalStorageDirectory(), Constants.DEX_NAME);
                        // 目标路径，私有目录里的临时文件夹odex
                        File targetFile = new File(getDir(Constants.DEX_DIR, Context.MODE_PRIVATE), Constants.DEX_NAME);
                        // 删除已经修复过的dex文件
                        if (targetFile.exists()) {
                            targetFile.delete();
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                        try {
                            // 复制dex文件
                            FileUtils.copyFile(sourceFile, targetFile);
                            HotFixUtils.loadFixDex(getApplication());
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                            Log.e("hotFIx", "loadFix");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    private void show() {
        ParamSort sort = new ParamSort();
        sort.math(this);
    }


    private static class MyHandler extends Handler {
        private WeakReference<Activity> activity;

        public MyHandler(Activity activity) {
            this.activity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("msg", msg.what + "");
            int what = msg.what;
            switch (what) {
                case 1:
                    Toast.makeText(activity.get(), "删除dex文件成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(activity.get(), "赋值dex文件成功", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    }

}
