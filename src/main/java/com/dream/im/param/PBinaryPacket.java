package com.dream.im.param;

import lombok.Data;

@Data
public class PBinaryPacket {

    private byte mark;

    private short nameLength;

    private short paramLength;

    private String name;

    private String params;
}
