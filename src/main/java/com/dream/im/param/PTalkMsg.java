package com.dream.im.param;

import lombok.Data;

@Data
public class PTalkMsg {

    private String message;

    private String senderName;

    private long timestamp;
}
