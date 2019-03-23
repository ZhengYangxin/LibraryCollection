package com.zyx.library.networklibrary.bean;

import com.zyx.library.networklibrary.type.NetType;

import java.lang.reflect.Method;

/**
 * 保存符合要求的网络监听注解方法
 */
public class MethodManager {

    // 参数类型NetType netType
    private Class<?> type;

    // 网络类型 netType = NetType.AUTO
    private NetType netType;

    // 方法
    private Method method;

    public MethodManager(Class<?> type, NetType netType, Method method) {
        this.type = type;
        this.netType = netType;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public NetType getNetType() {
        return netType;
    }

    public Method getMethod() {
        return method;
    }
}
