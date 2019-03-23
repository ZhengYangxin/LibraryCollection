package com.zyx.library.binderlibrary.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pielan
 * @date 12/3/19 下午2:41
 * @dec
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Execute {
    ExecuteMode excute() default ExecuteMode.MAIN;
}
