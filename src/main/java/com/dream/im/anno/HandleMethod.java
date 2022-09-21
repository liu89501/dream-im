package com.dream.im.anno;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleMethod {

    String value();

    boolean authenticate() default true;
}
