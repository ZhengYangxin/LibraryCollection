package com.zyx.library.binderlibrary;

/**
 * @author pielan
 * @date 13/3/19 上午11:25
 * @dec 接受请求处理，主应用进程
 */
public interface ResponseExecutor {

    ResponseExecutor NONE = new ResponseExecutor() {
        @Override
        public String execute(String request) {
            return null;
        }
    };

    String execute(String request);
}
