package com.dream.im;

public interface ImMessage {

    String getName();

    Class<? extends ImParameterBind<?>> getParameterBinderClass();
}
