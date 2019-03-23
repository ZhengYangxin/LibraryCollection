package com.zyx.library.eventbuslibrary;

import android.os.Looper;

/**
 * @author pielan
 * @date 14/3/19 下午11:19
 * @dec
 */
public interface MainThreadSupport {
    Poster createPoster(EventBus eventBus);

    boolean isMainThread();

    class AndroidHandlerMainThreadSupport implements MainThreadSupport {

        private final Looper looper;

        public AndroidHandlerMainThreadSupport(Looper looper) {
            this.looper = looper;
        }

        @Override
        public Poster createPoster(EventBus eventBus) {
            return new HandlerPoster(eventBus, looper, 10);
        }

        @Override
        public boolean isMainThread() {
            return looper == Looper.myLooper();
        }
    }

}
