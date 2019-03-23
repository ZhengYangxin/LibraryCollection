package com.zyx.library.networklibrary.annotation;

import com.zyx.library.networklibrary.type.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Network {

    NetType netType() default NetType.AUTO;
}
