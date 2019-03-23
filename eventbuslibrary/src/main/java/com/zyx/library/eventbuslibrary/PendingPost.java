package com.zyx.library.eventbuslibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pielan
 * @date 15/3/19 下午5:33
 * @dec
 */
public class PendingPost {
    private final static List<PendingPost> pendingPostPool = new ArrayList<PendingPost>();

    Object event;
    Subscription subscription;
    PendingPost next;

    public PendingPost(Object event, Subscription subscription) {
        this.event = event;
        this.subscription = subscription;
    }

    public static PendingPost obtainPendingPost(Subscription subscription, Object event) {

        // 从缓存中取，若缓存为空则new 一个
        synchronized (pendingPostPool) {
            int size = pendingPostPool.size();
            if (size > 0) {
                PendingPost pendingPost = pendingPostPool.remove(size - 1);
                pendingPost.event = event;
                pendingPost.subscription = subscription;
                pendingPost.next = null;
                return pendingPost;
            }
        }
        return new PendingPost(event, subscription);
    }

    public static void releasePendingPost(PendingPost pendingPost) {
        pendingPost.event = null;
        pendingPost.subscription = null;
        pendingPost.next = null;
        // 使用完毕，进行资源释放并放入缓存池
        synchronized (pendingPostPool) {
            if (pendingPostPool.size() < 1000) {
                pendingPostPool.add(pendingPost);
            }
        }
    }
}
