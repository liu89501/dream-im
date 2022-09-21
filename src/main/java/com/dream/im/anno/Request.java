package com.dream.im.anno;

import com.dream.im.constant.RequestMethod;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Request {

    String value() default "/";

    boolean authenticate() default true;

    RequestMethod method() default RequestMethod.GET;
}
