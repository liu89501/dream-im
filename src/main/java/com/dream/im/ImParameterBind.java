package com.dream.im;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Parameter;

public interface ImParameterBind<M extends ImMessage> {

    Object[] binding(ChannelHandlerContext ctx, Parameter[] parameters, M message) throws Exception;

}
