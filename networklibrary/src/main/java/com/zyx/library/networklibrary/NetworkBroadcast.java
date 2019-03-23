package com.zyx.library.networklibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.zyx.library.networklibrary.annotation.Network;
import com.zyx.library.networklibrary.bean.MethodManager;
import com.zyx.library.networklibrary.type.NetType;
import com.zyx.library.networklibrary.utils.Constancs;
import com.zyx.library.networklibrary.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 监听网路变化
 */
public class NetworkBroadcast extends BroadcastReceiver {

    private NetType netType;
//    private NetChangeObserver listener;
    private HashMap<Object, List<MethodManager>> networkList;

    public NetworkBroadcast() {
        netType = NetType.NONE;
        networkList = new HashMap<>();
    }

//    public void setListener(NetChangeObserver listener) {
//        this.listener = listener;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || intent.getAction() == null) {
            Log.e(Constancs.NET_LOG, "异常了");
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constancs.CONNECTIVITY_ACTION)) {
            Log.e(Constancs.NET_LOG, "  网络发生了变化");
            netType = NetworkUtils.getNetType(); // 网路类型
            if (NetworkUtils.isNetworkAvalible()) {
                Log.e(Constancs.NET_LOG, "网络连接成功");
//                listener.onConnect(netType);
            } else {
                Log.e(Constancs.NET_LOG, "网络连接断开");
//                listener.disConnect();
            }
            // 发送变化后的网络类型
            post(netType);
        }
    }

    private void post(NetType netType) {
        // 遍历注册的需要监听网络的类
        Set<Object> registerSets = networkList.keySet();
        for (Object register: registerSets) {
            // 得到类方法
            List<MethodManager> methodManagers = networkList.get(register);

            if (methodManagers != null) {
                for (MethodManager method : methodManagers) {
                    // 校验
                    if (method.getType().isAssignableFrom(netType.getClass())) {
                        switch (method.getNetType()) {
                            case AUTO:
                                invoke(method, register, netType);
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE)
                                invoke(method, register, netType);
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE)
                                    invoke(method, register, netType);
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE)
                                    invoke(method, register, netType);
                                break;
                            default:
                                break;
                        }
                    }

                }
            }
        }
    }

    private void invoke(MethodManager method, Object register, NetType netType) {
        // 获取方法
        Method execute = method.getMethod();
        try {
            // 调用
            execute.invoke(register, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void removeObserver(Object register) {
        if (!networkList.isEmpty() && networkList.containsKey(register)) {
            networkList.remove(register);
            Log.e(Constancs.NET_LOG, "注销成功");
        }
    }

    public void removeAllObserver() {
        networkList.clear();
        networkList = null;
    }

    public void registerObserver(Object register) {
        // 判断注册类是否已经注册
        List<MethodManager> methodManagers = networkList.get(register);
        if (methodManagers == null) {
            methodManagers = findAnnotationMethod(register);
            networkList.put(register, methodManagers);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object register) {
        List<MethodManager> methodManagers = new ArrayList<>();
        Class<?> clazz = register.getClass();
        // 获取注解方法
        Method[] methods = clazz.getMethods();
        if (methods != null) {
            for (Method method : methods) {
                Network network = method.getAnnotation(Network.class);
                if (network == null)
                    continue;

                // 得到注解方法返回值
                Type returnType = method.getGenericReturnType();

                if (!"void".equals(returnType.toString())) {
                    throw new RuntimeException("method return type must be void!");
                }

                // 得到注解方法参数
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("parameter  must be 1!");
                }

                MethodManager methodManager = new MethodManager(parameterTypes[0], network.netType(), method);
                methodManagers.add(methodManager);
            }
        }
        return methodManagers;
    }
}
