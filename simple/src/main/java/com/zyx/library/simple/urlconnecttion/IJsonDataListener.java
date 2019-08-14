package com.zyx.library.simple.urlconnecttion;

/**
 * @author pielan
 * @date 30/5/19 上午7:32
 * @dec
 */
public interface IJsonDataListener <T>{

    void onSuccess(T data);

    void onFail();
}
