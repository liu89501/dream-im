package com.dream.im;


public abstract class ImResults {

    private static final ImResult<?> DEFAULT_SUCCESS = new ImResult<>(true, null, null);
    private static final ImResult<?> DEFAULT_FAILURE = new ImResult<>(false, null, null);

    public static ImResult<?> success()
    {
        return DEFAULT_SUCCESS;
    }

    public static <T> ImResult<T> success(T data)
    {
        return new ImResult<>(true, null, data);
    }

    public static ImResult<?> successMsg(String msg)
    {
        return new ImResult<>(true, msg, null);
    }

    public static <T> ImResult<T> success(T data, String msg)
    {
        return new ImResult<>(true, msg, data);
    }

    public static ImResult<?> fail()
    {
        return DEFAULT_FAILURE;
    }

    public static <T> ImResult<T> fail(String msg)
    {
        return new ImResult<>(false, msg, null);
    }
}
