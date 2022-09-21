package com.dream.im.constant;

import com.dream.im.ImAuthenticatedInfo;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AttributeKey;

public interface ImConst {

    /**
     * 授权key
     */
    AttributeKey<ImAuthenticatedInfo> AUTHORIZATION_KEY = AttributeKey.valueOf("AUTHORIZATION");

    /**
     * 接收聊天消息
     */
    String RECEIVE_TALK_MESSAGE = "receive.msg.talk";


    String RECEIVE_FILE = "receive.file";

    /**
     * ByteBuf 中的前四个字节, 0x [FFFF (name实际字节长度)] [FFFF (params实际字节长度)]
     */
    int BINARY_FRAME_NAME_BYTES_LEN = 2;
    int BINARY_FRAME_PARAM_BYTES_LEN = 2;


    String WEBSOCKET_PATH = "/imws";


    String PK_USER_ID = "userId";
    String PK_SIGN = "sign";

}
