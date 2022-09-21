package com.dream.im;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class ImMessageBinary implements ImMessage {

    private ByteBuf byteBuf;

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<? extends ImParameterBind<?>> getParameterBinderClass() {
        return ImParameterBindBinary.class;
    }
}
