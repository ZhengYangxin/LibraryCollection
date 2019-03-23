package com.zyx.library.eventbuslibrary;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

/**
 * @author pielan
 * @date 14/3/19 下午11:04
 * @dec
 */
public class EventBus {

    public static final String TAG = "EventBus";

    static volatile EventBus defaultInstance;

    private static final EventBusBuilder DEFAULT_BUILDER = new EventBusBuilder();
    private static final Map<Class<?>, List<Class<?>>> eventTypeCache = new HashMap<>();

    private Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private Map<Object, List<Class<?>>> typesBySubscriber;

    private Map<Class<?>, Object> stickyEvents;

    private final ThreadLocal<PostingThreadState> currentPostingThreadState = new ThreadLocal<PostingThreadState>(){
        @Override
        protected PostingThreadState initialValue() {
            return new PostingThreadState();
        }
    };

    private MainThreadSupport mainThreadSupport;
    private Poster mainThreadPoster;
    private BackgroundPoster backgroundPoster;
    private AsyncPoster asyncPoster;
    private SubscriberMethodFinder subscriberMethodFinder;
    private ExecutorService executorService;

    private boolean throwSubscriberException;
    private boolean logSubscriberExceptions;
    private boolean logNoSubscriberMessages;
    private boolean sendSubscriberExceptionEvent;
    private boolean sendNoSubscriberEvent;
    private boolean eventInheritance;

    private int indexCount;
    private Logger logger;

    public static EventBus getDefault() {
        EventBus instance = defaultInstance;
        if (instance == null) {
            synchronized (EventBus.class) {
                instance = EventBus.defaultInstance;
                if (instance == null) {
                    instance = EventBus.defaultInstance = new EventBus();
                }
            }
        }
        return instance;
    }

    public static EventBusBuilder builder() {
        return new EventBusBuilder();
    }

    public static void clearCaches() {
        SubscriberMethodFinder.clearCaches();
        eventTypeCache.clear();
    }

    public EventBus() {
        this(DEFAULT_BUILDER);
    }

    public EventBus(EventBusBuilder builder) {
        logger = builder.getLogger();
        subscriptionsByEventType = new HashMap<>();
        typesBySubscriber = new HashMap<>();
        stickyEvents = new ConcurrentHashMap<>();
        mainThreadSupport = builder.getMainThreadSupport();
        mainThreadPoster = mainThreadSupport != null ? mainThreadSupport.createPoster(this) : null;
        backgroundPoster = new BackgroundPoster(this);
        asyncPoster = new AsyncPoster(this);
        indexCount = builder.subscriberInfoIndexes != null ? builder.subscriberInfoIndexes.size() : 0;
        subscriberMethodFinder = new SubscriberMethodFinder(builder.subscriberInfoIndexes,
                builder.strictMethodVerification, builder.ignoreGeneratedIndex);
        logSubscriberExceptions = builder.logSubscriberExceptions;
        logNoSubscriberMessages = builder.logNoSubscriberMessages;
        sendSubscriberExceptionEvent = builder.sendSubscriberExceptionEvent;
        sendNoSubscriberEvent = builder.sendNoSubscriberEvent;
        throwSubscriberException = builder.throwSubscriberException;
        eventInheritance = builder.eventInheritance;
        executorService = builder.executorService;

    }

    /**
     * 注册订阅类
     * @param subscriber 订阅对象
     */
    public void register(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        // 获取订阅对象的事件方法
        List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass);
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                // 进行绑定
                subscribe(subscriber, subscriberMethod);
            }
        }
    }

    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        // 获取到方法的event类型
        Class<?> eventType = subscriberMethod.eventType;
        // 创建一个订阅者和订阅方法的对象
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
        //以event类型为key，Subscriptions为value创建关联map
        //每一种事件，可能有多个订阅对象及方法
        CopyOnWriteArrayList<Subscription> subcriptions = subscriptionsByEventType.get(eventType);
        if (subcriptions == null) {
            subcriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType, subcriptions);
        } else {
            if (subcriptions.contains(newSubscription)) {
                throw new EventBusException("Subscriber" + subscriber.getClass() + "already register to event" + eventType);
            }
        }

        int size = subcriptions.size();
        for (int i = 0; i < size; i++) {
            if (i == size || subscriberMethod.priority > subcriptions.get(i).subscriberMethod.priority) {
                subcriptions.add(i, newSubscription);
                break;
            }
        }

        // 一个订阅者，定义的Event事件集合
        List<Class<?>> subscribedEvent = typesBySubscriber.get(subscriber);
        if (subscribedEvent == null) {
            subscribedEvent = new ArrayList<>();
            typesBySubscriber.put(subscriber, subscribedEvent);
        }
        subscribedEvent.add(eventType);

        if (subscriberMethod.sticky) {
            if (eventInheritance) {
                Set<Map.Entry<Class<?>, Object>> entries = stickyEvents.entrySet();
                for (Map.Entry<Class<?>, Object> entry : entries) {
                    Class<?> candidateEventType = entry.getKey();
                    if (eventType.isAssignableFrom(candidateEventType)) {
                        Object stickyEvent = entry.getValue();
                        checkPostStickEventToSubscription(newSubscription, stickyEvent);
                    }
                }
            } else {
                Object stickyEvent = stickyEvents.get(eventType);
                checkPostStickEventToSubscription(newSubscription, stickyEvent);
            }
        }

    }

    private void checkPostStickEventToSubscription(Subscription newSubscription, Object stickyEvent) {
        if (stickyEvent != null) {
            // 将粘性事件发送给订阅者
            postToSubscription(newSubscription, stickyEvent, isMainThread());
        }
    }

    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        // 判断事件线程模式
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING:
                invokeSubscriber(subscription, event);
                break;
            case MAIN:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                } else {
                    mainThreadPoster.enqueue(subscription, event);
                }
                break;
            case MAIN_ORDERED:
                if (mainThreadPoster != null) {
                    mainThreadPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            case BACKGROUND:
                if (isMainThread) {
                    backgroundPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            case ASYNC:
                asyncPoster.enqueue(subscription, event);
                break;
            default:
                throw new IllegalStateException("Unkonwn thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }

    // 执行事件方法
    private void invokeSubscriber(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // 处理异常
            handleSubscriberException(subscription, event, e.getCause());
        }
    }

    private void handleSubscriberException(Subscription subscription, Object event, Throwable cause) {
        if (event instanceof SubscriberExceptionEvent) {
            if (logSubscriberExceptions) {
                logger.log(Level.SEVERE, "SubscriberExceptionEvent subscriber " + subscription.subscriber.getClass()
                        + " threw an exception", cause);
                SubscriberExceptionEvent exEvent = (SubscriberExceptionEvent) event;
                logger.log(Level.SEVERE, "Initial event " + exEvent.causingEvent + " caused exception in "
                        + exEvent.causingSubscriber, exEvent.throwable);

            }
        } else {
            if (throwSubscriberException) {
                throw new EventBusException("Invoking subscriber failed", cause);
            }

            if (logSubscriberExceptions) {
                logger.log(Level.SEVERE, "Could not dispatch event: " + event.getClass() + " to subscribing class "
                        + subscription.subscriber.getClass(), cause);
            }

            // 发送异常事件
            if (sendSubscriberExceptionEvent) {
                SubscriberExceptionEvent exEvent = new SubscriberExceptionEvent(this, cause, event,
                        subscription.subscriber);
                post(exEvent);
            }
        }
    }

    private void post(Object event) {
        PostingThreadState postingState = currentPostingThreadState.get();
        List<Object> eventQueue = postingState.eventQueue;
        eventQueue.add(event);

        if (!postingState.isPosting) {
            postingState.isMainThread = isMainThread();
            postingState.isPosting = true;
            if (postingState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }

            try {
                while (!eventQueue.isEmpty()) {
                    postSingleEvent(eventQueue.remove(0), postingState);
                }
            } finally {
                postingState.isPosting = false;
                postingState.isMainThread = false;
            }
        }
    }

    private void postSingleEvent(Object event, PostingThreadState postingState) {
        Class<?> eventClass = event.getClass();
        boolean subscriptionFound = false;
        // 是否包括父类事件
        if (eventInheritance) {
            List<Class<?>> eventTypes = lookupAllEventTypes(eventClass);
            int countTypes = eventTypes.size();
            for (int h = 0; h < countTypes; h++) {
                Class<?> clazz = eventTypes.get(h);
                subscriptionFound |= postSingleEventForEventType(event, postingState, clazz);
            }
        } else {
            subscriptionFound = postSingleEventForEventType(event, postingState, event.getClass());
        }

        if (!subscriptionFound) {
            if (logNoSubscriberMessages) {
                logger.log(Level.FINE, "No subscribers registered for event " + eventClass);
            }
            if (sendNoSubscriberEvent && eventClass != NoSubscriberEvent.class && eventClass != SubscriberExceptionEvent.class) {
                post(new NoSubscriberEvent(this, event));
            }
        }
    }

    /**
     * 获取所有类，包括了父类及接口
     * @param eventClass
     * @return
     */
    private List<Class<?>> lookupAllEventTypes(Class<?> eventClass) {
        synchronized (eventTypeCache) {
            List<Class<?>> eventTypes = eventTypeCache.get(eventClass);
            if (eventTypes == null) {
                eventTypes = new ArrayList<>();
                Class<?> clazz = eventClass;
                while (clazz != null) {
                    eventTypes.add(clazz);
                    addInterfaces(eventTypes, clazz.getInterfaces());
                    clazz = clazz.getSuperclass();
                }
            }

            return eventTypes;
        }
    }

    /**
     * 获取父接口
     * @param eventTypes
     * @param interfaces
     */
    private void addInterfaces(List<Class<?>> eventTypes, Class<?>[] interfaces) {
        for (Class<?> interfacesClass : interfaces) {
            if (!eventTypes.contains(interfacesClass)) {
                eventTypes.add(interfacesClass);
                addInterfaces(eventTypes, interfacesClass.getInterfaces());
            }
        }
    }

    /**
     *
     * @param event 事件对象
     * @param postingState
     * @param eventClass 实现类，可能是父类或者接口类
     * @return
     */
    private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
        CopyOnWriteArrayList<Subscription> subcriptions;
        synchronized (this) {
            subcriptions = subscriptionsByEventType.get(eventClass);
        }

        if (subcriptions != null && !subcriptions.isEmpty()) {
            for (Subscription subscription : subcriptions) {
                postingState.event = event;
                postingState.subscription = subscription;
                boolean aborted = false;
                try {
                    postToSubscription(subscription, event, postingState.isMainThread);
                    aborted = postingState.canceled;
                } finally {
                    postingState.event = null;
                    postingState.subscription = null;
                    postingState.canceled = false;
                }

                if (aborted) {
                    break;
                }
            }
            return true;
        }
        return false;
    }


    private boolean isMainThread() {
        return mainThreadSupport != null ? mainThreadSupport.isMainThread() : true;
    }

    public void invokeSubscriber(PendingPost pendingPost) {
        Object event = pendingPost.event;
        Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);
        if (subscription.active) {
            invokeSubscriber(subscription, event);
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Logger getLogger() {
        return logger;
    }

    final static class PostingThreadState {
        final List<Object> eventQueue = new ArrayList<>();
        boolean isPosting;
        boolean isMainThread;
        Subscription subscription;
        Object event;
        boolean canceled;
    }
}


