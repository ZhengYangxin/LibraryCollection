package com.zyx.library.eventbuslibrary;

/**
 * @author pielan
 * @date 14/3/19 下午11:20
 * @dec
 */
public interface Poster {
    void enqueue(Subscription subscription, Object event);
}
