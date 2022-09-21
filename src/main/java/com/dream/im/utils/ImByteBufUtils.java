package com.dream.im.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.netty.buffer.ByteBuf;

public interface ImByteBufUtils {

    static ByteBuf fromObject(Object obj, ByteBuf byteBuf) throws JsonProcessingException
    {
        JsonMapper mapper = new JsonMapper();
        byte[] bytes = mapper.writeValueAsBytes(obj);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }
}
