package com.zyx.library.simple.urlconnecttion;

/**
 * @author pielan
 * @date 29/5/19 上午9:54
 * @dec
 */
public interface IHttpRequest {

    void setUrl(String url);

    void setData(byte[] data);

    void setListener(CallBackListener callBackListener);

    void execute();
}
