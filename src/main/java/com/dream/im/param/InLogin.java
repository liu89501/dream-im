package com.dream.im.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class InLogin {

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotBlank(message = "签名不能为空")
    private String sign;
}
