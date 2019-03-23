package com.zyx.library.eventbuslibrary;

import java.util.List;

/**
 * @author pielan
 * @date 14/3/19 下午11:17
 * @dec
 */
public class PostingThreadState {
    public List<Object> eventQueue;
    public boolean isPosting;
    public boolean isMainThread;
    public boolean canceled;
}
