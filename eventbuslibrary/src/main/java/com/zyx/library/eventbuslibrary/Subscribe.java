package com.zyx.library.eventbuslibrary;

/**
 * @author pielan
 * @date 15/3/19 下午3:42
 * @dec
 */
public @interface Subscribe {

    ThreadMode threadMode() default ThreadMode.MAIN;

    boolean sticky() default false;

    int priority() default 0;
}
