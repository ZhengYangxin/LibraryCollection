package com.zyx.library.aop.iinterface.imp;

import com.zyx.library.aop.iinterface.Status;

/**
 * @author pielan
 * @date 10/8/2019 2:11 PM
 * @dec
 */
public class JdkDynamic {

    public static void main(String[] args) {
        Status status = new JdkDynamicProxy(new JdkDynamicImp()).getProxy();
        status.execute("pielan");
    }
}

class JdkDynamicImp implements Status {
    @Override
    public void execute(String name) {
        System.out.print("Hello " + name);
    }
}