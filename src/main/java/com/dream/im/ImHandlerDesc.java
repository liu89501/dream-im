package com.dream.im;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImHandlerDesc {

    private Method classMethod;

    private Object instance;

    private String methodName;

    private boolean mustAuthenticate;
}
