package com.zyx.library.simple.urlconnecttion;

import java.io.InputStream;

/**
 * @author pielan
 * @date 29/5/19 上午9:56
 * @dec
 */
public interface CallBackListener {

    void onSuccess(InputStream inputStream);

    void onFailure();
}
