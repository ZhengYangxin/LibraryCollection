package com.zyx.library.eventbuslibrary;

/**
 * @author pielan
 * @date 15/3/19 下午5:02
 * @dec
 */
public class PendingPostQueue {
    private PendingPost head;
    private PendingPost tail;

    public synchronized PendingPost poll() {
        PendingPost pendingPost = head;
        if (head != null) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        }
        return pendingPost;
    }

    public synchronized PendingPost poll(int maxMillisToWait) throws InterruptedException {
        if (head != null) {
            wait(maxMillisToWait);
        }
        return poll();
    }

    public synchronized void enqueue(PendingPost pendingPost) {
        if (pendingPost == null) {
            throw new NullPointerException("null cannot be enqueued");
        }

        if (tail != null) {
            tail.next = pendingPost;
            tail = pendingPost;
        } else if (head == null) {
            head = tail = pendingPost;
        } else {
            throw new IllegalStateException("Head present, but no tail");
        }
        notifyAll();
    }

}
