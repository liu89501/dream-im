package com.dream.im.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface ParamUtils {

    /**
     * 解析请求路径后面的参数
     * @param requestUri    请求路径
     * @return  参数集
     */
    static Map<String, String> parseParams(String requestUri)
    {
        int searchIndex = requestUri.indexOf('?');
        if (searchIndex == -1)
        {
            return Collections.emptyMap();
        }

        HashMap<String, String> map = new HashMap<>();

        String params = requestUri.substring(searchIndex + 1);
        String[] kvp = params.split("&");

        for (String kv : kvp) {
            String[] kvArr = kv.split("=");
            map.put(kvArr[0], kvArr[1]);
        }

        return map;
    }
}
