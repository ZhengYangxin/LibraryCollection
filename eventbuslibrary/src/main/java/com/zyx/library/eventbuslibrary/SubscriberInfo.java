package com.zyx.library.eventbuslibrary;

/**
 * @author pielan
 * @date 15/3/19 下午1:17
 * @dec
 */
public interface SubscriberInfo {
    SubscriberInfo getSuperSubscriberInfo();

    Class<?> getSubscriberClass();

    SubscriberMethod[] getSubscriberMethods();
}
