package com.dream.im;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Parameter;

public class ImParameterBindBinary implements ImParameterBind<ImMessageBinary> {

    @Override
    public Object[] binding(ChannelHandlerContext ctx, Parameter[] parameters, ImMessageBinary message) throws Exception {

        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++)
        {
            Class<?> parameterType = parameters[i].getType();

            Object arg;

            if (parameterType == ByteBuf.class)
            {
                arg = message.getByteBuf();
            }
            else if (parameterType == Channel.class)
            {
                arg = ctx.channel();
            }
            else
            {
                throw new IllegalArgumentException("仅支持ByteBuf和Channel参数");
            }

            args[i] = arg;
        }

        return args;
    }
}
