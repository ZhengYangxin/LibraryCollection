package com.zyx.library.simple.urlconnecttion;

import android.os.Handler;
import android.os.Looper;
import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author pielan
 * @date 29/5/19 下午12:50
 * @dec
 */
public class JsonCallBackListener<T> implements CallBackListener{


    private Class<T> responseClass;
    private Handler handler = new Handler(Looper.getMainLooper());
    private IJsonDataListener jsonDataListener;

    public JsonCallBackListener(Class<T> responseClass, IJsonDataListener jsonDataListener) {
        this.responseClass = responseClass;
        this.jsonDataListener = jsonDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        // 将流转换为对应数据类型
        String response = getContent(inputStream);
        final T clazz = JSON.parseObject(response, responseClass);
        handler.post(new Runnable() {
            @Override
            public void run() {
                jsonDataListener.onSuccess(clazz);
            }
        });
        
    }

    private String getContent(InputStream inputStream) {
        String content = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer sb = new StringBuffer();

            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            content =  sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public void onFailure() {

    }
}
