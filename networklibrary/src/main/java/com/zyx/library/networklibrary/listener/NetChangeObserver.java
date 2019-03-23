package com.zyx.library.networklibrary.listener;

import com.zyx.library.networklibrary.type.NetType;

/**
 * 网络状态变化
 */
public interface NetChangeObserver {

    void onConnect(NetType netType);

    void disConnect();
}
