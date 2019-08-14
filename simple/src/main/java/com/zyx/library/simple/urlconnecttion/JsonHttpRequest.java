package com.zyx.library.simple.urlconnecttion;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author pielan
 * @date 29/5/19 上午10:07
 * @dec
 */
public class JsonHttpRequest implements IHttpRequest {

    private String url;

    private byte[] data;

    private CallBackListener callBackListener;

    private HttpURLConnection httpURLConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    @Override
    public void execute() {
        URL url = null;

        try {
            url = new URL(this.url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setReadTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-type", "application/json;charset=utf-8");
            httpURLConnection.connect();

            OutputStream out = httpURLConnection.getOutputStream();
            BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
            bufferedOut.write(data);
            bufferedOut.flush();
            bufferedOut.close();
            out.close();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream input = httpURLConnection.getInputStream();
                callBackListener.onSuccess(input);
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
