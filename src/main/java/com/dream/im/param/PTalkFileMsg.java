package com.dream.im.param;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class PTalkFileMsg {

    private String message;

    private String senderName;

    private long fileSize;

    private String fileName;

    private String fileType;

    private long timestamp;
}
