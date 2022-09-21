package com.dream.im.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InTalkMessageFile extends InTalkMessage {

    private long fileSize;

    private String fileName;

    private String fileType;
}
