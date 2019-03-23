package com.zyx.library.binderlibrary;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.reflect.Proxy;

/**
 * @author pielan
 * @date 10/3/19 下午9:54
 * @dec
 */
public class ProcessManager {

    /*————————————————————————主进程——————————————————————————————————————————*/
    private CacheCenter cacheCenter ;

    private static ProcessManagerBuilder builder = new ProcessManagerBuilder();

    private static ProcessManager ourInstance = new ProcessManager();

    public static ProcessManager getInstance( ) {
        return ourInstance;
    }

    public ProcessManager() {
        this(builder);
    }

    ProcessManager(ProcessManagerBuilder builder) {
        cacheCenter = builder.cacheCenter;
    }

    /**
     * 注册远程调用接口类
     * @param clazz
     */
    public void register(Class<?> clazz) {
        cacheCenter.register(clazz);
    }

    public void register(Class<?> clazz, Object object) {
        cacheCenter.registerInstance(clazz, object);
    }

    /*————————————————————————另一个进程——————————————————————————————————————————*/

    /**
     * 请求建立实例缓存，通过动态代理生成代理对象
     * @param clazz 接口类
     * @param parameters 可变参数（构造函数）
     * @return
     */
    public <T> T getInstance(Class<T> clazz, Object... parameters) {
//        builder.requestExecutor.sendRequest(Constants.GET_INSTANCE, clazz, null, parameters);
        T proxy = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},  new ProcessInvocationHandler(clazz, builder));
        return proxy;
    }

    /**
     * 建立binder通讯，指定包名
     * @param context
     * @param packageName 包名
     */
    public void connect(Context context, String packageName) {
        builder = procManager().setRequestExecutor(new RequestExecute(new BinderProcessImpl(context, packageName)));
    }

    /**
     * 建立binder通讯
     * @param context
     */
    public void connect(Context context) {
        builder = procManager().setRequestExecutor(new RequestExecute(new BinderProcessImpl(context)));
    }

    /**
     * 建立通讯连接可以为binder，socket，http等
     */
    public void connect(IprocessInterface processInterface) {
        builder = procManager().setRequestExecutor(new RequestExecute(processInterface));
    }

    public static @NonNull ProcessManagerBuilder procManager() {
        return builder == null ? new ProcessManagerBuilder() : builder;
    }

    public void registerLocal(Class<?> clazz, Object object) {
        cacheCenter.registerInstance(clazz, object);
    }
}
