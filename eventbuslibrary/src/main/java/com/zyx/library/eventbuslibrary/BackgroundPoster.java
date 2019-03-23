package com.zyx.library.eventbuslibrary;

import java.util.logging.Level;

/**
 * @author pielan
 * @date 14/3/19 下午11:20
 * @dec
 */
public class BackgroundPoster implements Runnable, Poster{

    private final PendingPostQueue queue;
    private final EventBus eventBus;

    private volatile boolean executorRunning;

    public BackgroundPoster(EventBus eventBus) {
        this.eventBus = eventBus;
        queue = new PendingPostQueue();
    }

    public void enqueue(Subscription subscription, Object event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);
        synchronized (this) {
            queue.enqueue(pendingPost);
            if (!executorRunning) {
                executorRunning = true;
                eventBus.getExecutorService().execute(this);
            }
        }
    }

    @Override
    public void run() {
        try {
            try {
                while (true) {
                    PendingPost pendingPost = queue.poll();
                    if (pendingPost == null) {
                        synchronized (this) {
                            pendingPost = queue.poll();
                            if (pendingPost == null) {
                                executorRunning = false;
                            }
                        }
                    }
                    eventBus.invokeSubscriber(pendingPost);
                }
            } catch (Exception e) {
                eventBus.getLogger().log(Level.WARNING, Thread.currentThread().getName() + " was interruppted", e);
            }
        } finally {
            executorRunning = false;
        }
    }
}
