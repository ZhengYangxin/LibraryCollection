package com.zyx.library.binderlibrary;

import android.text.TextUtils;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author pielan
 * @date 10/3/19 下午11:29
 * @dec 生成动态代理，其他进程的方法调用都会走这里
 */
public class ProcessInvocationHandler implements InvocationHandler {
    private Class<?> clazz;
    private static final Gson gson = new Gson();
    private ProcessManagerBuilder builder;
    public ProcessInvocationHandler(Class<?> clazz, ProcessManagerBuilder builder) {
        this.clazz = clazz;
        this.builder = builder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String response = builder.requestExecutor.sendRequest(Constants.GET_METHOD, clazz, method, args);
        if (!TextUtils.isEmpty(response) && !"null".equals(response)) {
            Class methodClass = method.getReturnType();
            Object object =  gson.fromJson(response, methodClass);
            return object;
        }
        return null;
    }
}
