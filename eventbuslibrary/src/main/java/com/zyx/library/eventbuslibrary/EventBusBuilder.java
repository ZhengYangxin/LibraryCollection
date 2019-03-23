package com.zyx.library.eventbuslibrary;

import android.os.Looper;

import java.util.AbstractCollection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author pielan
 * @date 14/3/19 下午11:09
 * @dec
 */
public class EventBusBuilder {
    private final static ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    public AbstractCollection subscriberInfoIndexes;
    public Object strictMethodVerification;
    public boolean logSubscriberExceptions;
    public boolean logNoSubscriberMessages;
    public boolean sendNoSubscriberEvent;
    public boolean throwSubscriberException;
    public boolean eventInheritance;
    public ExecutorService executorService;
    public boolean sendSubscriberExceptionEvent;
    public Object ignoreGeneratedIndex;

    MainThreadSupport mainThreadSupport;
    Logger logger;

    public EventBusBuilder() {
    }

    public Logger getLogger() {
        if (logger != null) {
            return logger;
        } else {
            return AndroidLogger.isAndroidLogAvailable() && getAndroidMainLooperOrNull() != null ?
                    new AndroidLogger("EventBus") :
                    new Logger.SystemOutLogger();
        }
    }

    public MainThreadSupport getMainThreadSupport() {
        if (mainThreadSupport != null) {
            return mainThreadSupport;
        }else {
            Object looperOrNull = getAndroidMainLooperOrNull();
            return looperOrNull == null ? null : new MainThreadSupport.AndroidHandlerMainThreadSupport((Looper) looperOrNull);
        }
    }

    private Object getAndroidMainLooperOrNull() {
        try {
            return Looper.getMainLooper();
        } catch (RuntimeException e) {
            return null;
        }
    }
}
