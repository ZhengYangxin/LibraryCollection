package com.zyx.library.binderlibrary.bean;

/**
 * @author pielan
 * @date 10/3/19 下午9:48
 * @dec
 */
public class RequestParameter {

    private String parameterClassName;
    private String parameterValue;

    public RequestParameter() {

    }

    public RequestParameter(String parameterClassName, String parameterValue) {
        this.parameterClassName = parameterClassName;
        this.parameterValue = parameterValue;
    }

    public String getParameterClassName() {
        return parameterClassName;
    }

    public void setParameterClassName(String parameterClassName) {
        this.parameterClassName = parameterClassName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
