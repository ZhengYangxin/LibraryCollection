package com.zyx.library.hotfixlibrary.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * @author pielan
 * @date 7/3/19 下午1:12
 * @dec
 */
public class ReflectUtils {


    /**
     * 合并数组
     * @param arrayLhs 前数组（插队数组
     * @param arrayRhs 后数组（已有数组）
     * @return
     */
    public static Object combileArray(Object arrayLhs, Object arrayRhs) {

        //获得一个数组的class对象，通过Array.newInstance()可以反射生成数组对象
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        // 获取前数组长度
        int i = Array.getLength(arrayLhs);
        // 新数组总长度
        int j = i + Array.getLength(arrayRhs);
        // 新的的数组对象
        Object result = Array.newInstance(localClass, j);

        for (int k = 0; k < j; k++) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs,  k - i));
            }
        }

        return result;
    }

    /**
     * 通过反射获取BaseDexClassLoaderder对象，在获取dexElements对象
     * @param paramObject
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getDexElements(Object paramObject) throws NoSuchFieldException, IllegalAccessException {
        return getfield(paramObject, paramObject.getClass(), "dexElements");
    }

    /**
     * 通过反射获取某对象，并设置私有可访问
     * @param obj 该属性所属类的对象
     * @param clazz 该属性所属类
     * @param field 属性名
     * @return 该属性对象
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getfield(Object obj, Class<?> clazz, String field) throws NoSuchFieldException, IllegalAccessException {
        Field localField = clazz.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    /**
     * 通过反射获取BaseDexClassLoader对象中的PathList对象
     * @param baseDexClassLoader BaseDexClassLoader对象
     * @return PathList对象
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getPathList(Object baseDexClassLoader) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getfield(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 给某属性赋值，并设置私有可访问
     * @param obj 该属性所属类的对象
     * @param clazz 该属性所属类
     * @param value 值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setFiled(Object obj, Class<?> clazz, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field localField = clazz.getDeclaredField("dexElements");
        localField.setAccessible(true);
        localField.set(obj, value);
    }
}
