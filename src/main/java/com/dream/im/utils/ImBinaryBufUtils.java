package com.dream.im.utils;

import com.dream.im.constant.ImConst;
import com.dream.im.param.PBinaryPacket;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public interface ImBinaryBufUtils {

    /**
     * 获取二进制消息中的Name, 不改变byteBuf索引
     * @param byteBuf   buf
     * @return  name
     */
    static String getMessageName(ByteBuf byteBuf)
    {
        if (byteBuf != null)
        {
            byte[] name = new byte[byteBuf.getShortLE(1)];
            byteBuf.getBytes(ImConst.BINARY_FRAME_NAME_BYTES_LEN + ImConst.BINARY_FRAME_PARAM_BYTES_LEN + 1, name);
            return new String(name, StandardCharsets.UTF_8);
        }

        return null;
    }

    static PBinaryPacket readBasePacket(ByteBuf byteBuf)
    {
        if (byteBuf != null)
        {
            PBinaryPacket packet = new PBinaryPacket();
            packet.setMark(byteBuf.readByte());
            packet.setNameLength(byteBuf.readShortLE());
            packet.setParamLength(byteBuf.readShortLE());

            byte[] nameBytes = new byte[packet.getNameLength()];
            byteBuf.readBytes(nameBytes);
            packet.setName(new String(nameBytes, StandardCharsets.UTF_8));

            byte[] paramsBytes = new byte[packet.getParamLength()];
            byteBuf.readBytes(paramsBytes);
            packet.setParams(new String(paramsBytes, StandardCharsets.UTF_8));

            return packet;
        }

        return null;
    }
}
