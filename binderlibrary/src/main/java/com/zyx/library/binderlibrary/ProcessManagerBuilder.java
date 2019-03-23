package com.zyx.library.binderlibrary;

/**
 * @author pielan
 * @date 11/3/19 下午5:36
 * @dec
 */
public class ProcessManagerBuilder {

    // 注册类中心
    public CacheCenter cacheCenter = CacheCenter.getInstance();

    // 请求实现类
    public RequestExecutor requestExecutor = RequestExecutor.NONE;

    public ProcessManagerBuilder() {
    }

    public ProcessManagerBuilder setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
        return this;
    }

}
