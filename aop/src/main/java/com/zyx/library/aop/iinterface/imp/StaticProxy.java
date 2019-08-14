package com.zyx.library.aop.iinterface.imp;

import com.zyx.library.aop.iinterface.Status;

/**
 * @author pielan
 * @date 10/8/2019 1:48 PM
 * @dec
 */
public class StaticProxy {

    class StaticProxyStatusImp implements Status {

        @Override
        public void execute(String name) {
            System.out.print("Hello " + name);
        }
    }

    class StatusProxy implements Status {

        private Status status;

        public StatusProxy(Status status) {
            before();
            this.status = status;
            after();
        }

        @Override
        public void execute(String name) {
            status.execute(name);
        }

        private void after() {
            System.out.print("after ");
        }

        private void before() {
            System.out.print("before ");

        }
    }
}
