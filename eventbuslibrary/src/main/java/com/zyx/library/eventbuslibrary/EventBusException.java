package com.zyx.library.eventbuslibrary;

/**
 * @author pielan
 * @date 14/3/19 下午11:49
 * @dec
 */
public class EventBusException extends RuntimeException {
    public EventBusException(String s) {
    }

    public EventBusException(String invoking_subscriber_failed, Throwable cause) {
    }
}
