package com.zyx.library.binderlibrary;

import android.util.Log;
import com.google.gson.Gson;
import com.zyx.library.binderlibrary.annotion.ClassId;
import com.zyx.library.binderlibrary.annotion.Execute;
import com.zyx.library.binderlibrary.annotion.ExecuteMode;
import com.zyx.library.binderlibrary.bean.RequestBean;
import com.zyx.library.binderlibrary.bean.RequestParameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author pielan
 * @date 13/3/19 上午11:02
 * @dec
 */
public class RequestExecute implements RequestExecutor{

    private IprocessInterface processInterface;

    private static Gson gson = new Gson();

    public RequestExecute(IprocessInterface processInterface) {
        this.processInterface = processInterface;
    }
    /**
     * 将请求数据包装成bean对象后转成json
     * @param type 请求类型，1 实例对象的调用：2 请求方法
     * @param clazz 调用对象的class类型
     * @param method 调用的方法
     * @param parameters 调用的参数
     * @return
     */
    @Override
    public String sendRequest(int type, Class<?> clazz, Method method, Object[] parameters) {
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            requestParameters = new RequestParameter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = gson.toJson(parameter);
                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i] = requestParameter;
            }
        }

        String className = clazz.getAnnotation(ClassId.class).value();
        String methodName = method == null? "" : method.getName();
        if (method != null) {
            Execute execute = method.getAnnotation(Execute.class);
            if (execute != null) {
                ExecuteMode mode = execute.excute();
                switch (mode) {
                    case BASE:
                        Log.i("pielan", "BASE");
                        break;
                    case LOCAL:
                        Object object = CacheCenter.getInstance().getLocalInstance(clazz);
                        try {
                            Object dataObject = method.invoke(object, parameters);
                            String responseData = gson.toJson(dataObject);
                            return responseData;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        Log.i("pielan", "LOCAL");
                        break;
                    default:
                        break;
                }
            } else {
                Log.i("pielan", "MAIN");
                RequestBean requestBean = new RequestBean(type, className, methodName, requestParameters);
                String request = gson.toJson(requestBean);
                return processInterface.send(request);
            }
        }
        return null;
    }
}
