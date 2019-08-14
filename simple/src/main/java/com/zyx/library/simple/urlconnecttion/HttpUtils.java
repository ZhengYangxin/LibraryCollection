package com.zyx.library.simple.urlconnecttion;

/**
 * @author pielan
 * @date 30/5/19 上午7:42
 * @dec
 */
public class HttpUtils {

    public static <T, M> void sendJsonRequest(String url, T requestData, Class<M> response, IJsonDataListener jsonDataListener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallBackListener callBackListener = new JsonCallBackListener<>(response, jsonDataListener);
        HttpTask httpTask = new HttpTask(url, requestData, httpRequest, callBackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
