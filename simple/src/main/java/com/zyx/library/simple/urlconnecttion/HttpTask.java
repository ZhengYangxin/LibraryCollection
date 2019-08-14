package com.zyx.library.simple.urlconnecttion;

import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author pielan
 * @date 29/5/19 上午10:00
 * @dec 包装了Http请求的异步任务
 */
public class HttpTask<T> implements Runnable, Delayed {

    IHttpRequest request;

    public HttpTask(String url, T requestData, IHttpRequest request, CallBackListener callBackListener) {
        this.request = request;
        request.setUrl(url);
        request.setListener(callBackListener);
        String content = JSON.toJSONString(requestData);
        try {
            request.setData(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            request.execute();
        } catch (Exception e) {
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }


    // 延迟时间
    private long delayTime;

    // 重试次数
    private int retryCount;

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public long getDelay(@NonNull TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
    }

    @Override
    public int compareTo(@NonNull Delayed o) {
        return 0;
    }
}
