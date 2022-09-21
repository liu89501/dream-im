package com.dream.im.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.util.Base64;
import java.util.UUID;

public interface SecretUtils {

    static String encrypt(String secretKey, String userId)
    {
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey);
        return Base64.getEncoder().encodeToString(hmacUtils.hmac(userId));
    }

    static String generateSecretKey()
    {
        return (UUID.randomUUID() + UUID.randomUUID().toString()).replaceAll("-", "");
    }
}
