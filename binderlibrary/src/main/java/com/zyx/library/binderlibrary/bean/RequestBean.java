package com.zyx.library.binderlibrary.bean;

/**
 * @author pielan
 * @date 10/3/19 下午9:46
 * @dec
 */
public class RequestBean {
    private int type;
    private String className;
    private String mehodName;

    private RequestParameter[] requestParameters;

    public RequestBean() {

    }

    public RequestBean(int type, String className, String mehodName, RequestParameter[] requestParameters) {
        this.type = type;
        this.className = className;
        this.mehodName = mehodName;
        this.requestParameters = requestParameters;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMehodName() {
        return mehodName;
    }

    public void setMehodName(String mehodName) {
        this.mehodName = mehodName;
    }

    public RequestParameter[] getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(RequestParameter[] requestParameters) {
        this.requestParameters = requestParameters;
    }

    public int getType() {
        return type;
    }
}
