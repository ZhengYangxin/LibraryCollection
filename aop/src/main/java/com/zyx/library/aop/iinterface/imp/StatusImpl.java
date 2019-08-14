package com.zyx.library.aop.iinterface.imp;

import com.zyx.library.aop.iinterface.Status;

/**
 * @author pielan
 * @date 10/8/2019 1:34 PM
 * @dec
 */
public class StatusImpl implements Status {
    @Override
    public void execute(String name) {
        before();
        System.out.print("Hello " + name);
        after();
    }

    private void after() {
        System.out.print("after ");
    }

    private void before() {
        System.out.print("before ");

    }
}
