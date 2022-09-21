package com.dream.im;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImResult<T> {

    private boolean success;

    private String msg;

    private T data;
}
