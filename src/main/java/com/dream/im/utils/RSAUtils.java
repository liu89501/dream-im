package com.dream.im.utils;

import lombok.SneakyThrows;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public interface RSAUtils {

    @SneakyThrows
    static void printKeyPair() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        System.out.println("PublicKey: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        System.out.println("PrivateKey: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
    }

    static String encryptPublic(String content, String publicKey) {
        try {
            byte[] encodePublic = Base64.getEncoder().encode(publicKey.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new RSAPublicKeyImpl(encodePublic));

            byte[] bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String decryptPrivate(String content, String privateKey) {
        try {
            byte[] contentBytes = Base64.getEncoder().encode(privateKey.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(contentBytes)));
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(content));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
