package com.dream.im;

import com.dream.im.constant.RequestMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImHandlerDescHttp {

    private Method classMethod;

    private Object instance;

    private boolean authenticate;

    private RequestMethod requestMethod;
}
