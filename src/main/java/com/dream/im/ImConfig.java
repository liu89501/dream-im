package com.dream.im;

import com.dream.container.anno.Config;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@Config(classpath = "im-config.json")
public class ImConfig {

    @JsonProperty("auth-timeout")
    private int authTimeout;
}
