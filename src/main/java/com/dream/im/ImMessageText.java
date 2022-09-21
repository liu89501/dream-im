package com.dream.im;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImMessageText implements ImMessage {

    private String name;

    private String data;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<? extends ImParameterBind<?>> getParameterBinderClass() {
        return ImParameterBindText.class;
    }
}
