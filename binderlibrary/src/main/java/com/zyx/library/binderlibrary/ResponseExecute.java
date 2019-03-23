package com.zyx.library.binderlibrary;

import com.google.gson.Gson;
import com.zyx.library.binderlibrary.bean.RequestBean;
import com.zyx.library.binderlibrary.bean.RequestParameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author pielan
 * @date 13/3/19 上午10:49
 * @dec 请求处理，返回数据
 */
public class ResponseExecute implements ResponseExecutor{

    private static Gson gson = new Gson();

    @Override
    public String execute(String request) {
        RequestBean requestBean = gson.fromJson(request, RequestBean.class);
        switch (requestBean.getType()) {
            case Constants.GET_INSTANCE:
                Method instanceMethod = CacheCenter.getInstance().getMethod(requestBean.getClassName(), "getInstance");
                if (instanceMethod == null) {
                    return null;
                }
                Object[] objects = makeParameterObject(requestBean);
                try {
                    Object manager = instanceMethod.invoke(null, objects);
                    CacheCenter.getInstance().putObject(requestBean.getClassName(), manager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.GET_METHOD:
                Object object = CacheCenter.getInstance().getInstanceByClassName(requestBean.getClassName());
                Method invokeMethod = CacheCenter.getInstance().getMethod(requestBean.getClassName(),  requestBean.getMehodName());
                if (invokeMethod == null) {
                    throw new RuntimeException(invokeMethod.getName() + " is null");
                }
                try {
                    Object dataObject = invokeMethod.invoke(object, makeParameterObject(requestBean));
                    String responseData = gson.toJson(dataObject);
                    return responseData;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                break;
        }

        return null;
    }


    /**
     * 将参数值，转化为对应的对象
     * @param requestBean
     * @return
     */
    private static Object[] makeParameterObject(RequestBean requestBean) {
        Object[] mParameters = null;
        RequestParameter[] requestParameters = requestBean.getRequestParameters();
        if (requestParameters != null && requestParameters.length > 0) {
            mParameters = new Object[requestParameters.length];
            for (int i = 0; i < requestParameters.length; i++) {
                RequestParameter requestParameter = requestParameters[i];
                Class<?> clazz = CacheCenter.getInstance().getCalssType(requestParameter.getParameterClassName());
                mParameters[i] = gson.fromJson(requestParameter.getParameterValue(), clazz);
            }
        } else {
            mParameters = new Object[0];
        }

        return mParameters;
    }


}
