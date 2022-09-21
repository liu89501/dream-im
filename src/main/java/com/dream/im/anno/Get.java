package com.dream.im.anno;

import com.dream.im.constant.RequestMethod;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Request(method = RequestMethod.GET)
public @interface Get {

    String value();
    boolean authenticate() default true;
}
