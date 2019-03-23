package com.zyx.library.binderlibrary;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author pielan
 * @date 10/3/19 下午12:58
 * @dec 缓存中心
 */
public class CacheCenter {

    private HashMap<String, Class<?>> mClasses = new HashMap<>(); //类名和类的映射
    private HashMap<Class<?>, HashMap<String, Method>> mMethods= new HashMap<>(); //类和方法的映射
    private HashMap<String, Object> mObjects = new HashMap<>(); //类名和类实例的映射
    private HashMap<Class<?>, Object> mInstance = new HashMap<>(); //类和类实例的映射

    private CacheCenter() {
    }

    private static CacheCenter instance = new CacheCenter();

    public static CacheCenter getInstance() {
        return instance;
    }

    public void register(Class<?> clazz) {
        mClasses.put(clazz.getName(), clazz);
        registerMethods(clazz);
    }

    public Object getObject(String name) {
        return mObjects.get(name);
    }

    public void putObject(String name, Object object) {
        mObjects.put(name, object);
    }

    private void registerMethods(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (mMethods.get(clazz) == null) {
                mMethods.put(clazz, new HashMap<String, Method>());
            }
            HashMap<String, Method> map = mMethods.get(clazz);
            map.put(method.getName(), method);
        }
    }

//    @TargetApi(Build.VERSION_CODES.N)
    public Method getMethod(String className, String name) {
        Class clazz = getCalssType(className);
        if (!TextUtils.isEmpty(name)) {
            if (mMethods.get(clazz) == null) {
                mMethods.put(clazz, new HashMap<String, Method>());
            }
            HashMap<String, Method> methods = mMethods.get(clazz);
            Method method = methods.get(name);
            if (method != null) {
                return method;
            }
//            mMethods.putIfAbsent(clazz, new HashMap<String, Method>(String, Method));
        }

        return null;
    }

    public Class getCalssType(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        Class<?> clazz = mClasses.get(name);
        if (clazz == null) {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }


    public void registerInstance(Class<?> clazz, Object object) {
        register(clazz);
        mInstance.put(clazz, object);
    }

    public Object getLocalInstance(Class<?> clazz) {
        return mInstance.get(clazz);
    }

    public Object getInstanceByClassName(String className) {
        return mInstance.get(getCalssType(className));
    }
}
