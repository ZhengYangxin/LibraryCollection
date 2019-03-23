package com.zyx.library.eventbuslibrary;

/**
 * @author pielan
 * @date 15/3/19 上午12:51
 * @dec
 */
public class SubscriberExceptionEvent {
    public String causingSubscriber;
    public Throwable throwable;
    public String causingEvent;

    public SubscriberExceptionEvent(EventBus eventBus, Throwable cause, Object event, Object subscriber) {
    }
}
