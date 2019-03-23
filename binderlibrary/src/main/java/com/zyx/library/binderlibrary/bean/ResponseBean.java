package com.zyx.library.binderlibrary.bean;

/**
 * @author pielan
 * @date 10/3/19 下午9:44
 * @dec
 */
public class ResponseBean {
    private Object data;

    public ResponseBean(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
