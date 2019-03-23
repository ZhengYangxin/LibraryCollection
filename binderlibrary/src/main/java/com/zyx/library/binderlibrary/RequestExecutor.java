package com.zyx.library.binderlibrary;

import java.lang.reflect.Method;

/**
 * @author pielan
 * @date 13/3/19 上午11:25
 * @dec 请求处理
 */
public interface RequestExecutor {

    RequestExecutor NONE = new RequestExecutor() {
        @Override
        public String sendRequest(int type, Class<?> clazz, Method method, Object[] parameters) {
            return null;
        }
    };

    String sendRequest(int type, Class<?> clazz, Method method, Object[] parameters);
}
