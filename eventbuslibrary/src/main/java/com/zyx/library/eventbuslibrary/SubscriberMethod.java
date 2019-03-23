package com.zyx.library.eventbuslibrary;

import java.lang.reflect.Method;

/**
 * @author pielan
 * @date 14/3/19 下午11:39
 * @dec
 */
public class SubscriberMethod {
    public Class<?> eventType;
    public int priority;
    public boolean sticky;
    public ThreadMode threadMode;
    public Method method;

    public SubscriberMethod(Method method, Class<?> eventType, ThreadMode threadMode, int priority, boolean sticky) {
        this.eventType = eventType;
        this.priority = priority;
        this.sticky = sticky;
        this.threadMode = threadMode;
        this.method = method;
    }
}
