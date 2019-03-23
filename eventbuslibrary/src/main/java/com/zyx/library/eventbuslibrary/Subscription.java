package com.zyx.library.eventbuslibrary;

/**
 * @author pielan
 * @date 14/3/19 下午11:14
 * @dec
 */
public class Subscription {
    public SubscriberMethod subscriberMethod;
    public Object subscriber;
    volatile boolean active;


    public Subscription(Object subscriber, SubscriberMethod subscriberMethod) {
        this.subscriberMethod = subscriberMethod;
        this.subscriber = subscriber;
        this.active = true;
    }
}
