package com.security.university.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final public class CustomHashAlgorithms {
    private static String prepare(String hashAlgorithm, String open_text) {
        StringBuilder stringBuilder;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
            byte[] open_text_bytes = open_text.getBytes();
            messageDigest.update(open_text_bytes);
            byte[] data = messageDigest.digest();
            stringBuilder = new StringBuilder();
            for (byte byteData : data) {
                stringBuilder.append(Integer.toString((byteData & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        return stringBuilder.toString();
    }

    public static String md5(String open_text) {
        return prepare("MD5", open_text);
    }

    public static String sha256(String open_text) {
        return prepare("SHA-256", open_text);
    }

    public static String sha1(String open_text) {
        return prepare("SHA-1", open_text);
    }

    public static String sha384(String open_text) {
        return prepare("SHA-384", open_text);
    }

    public static String customAlgorithm(String open_text, String algorithm) {
        return prepare(algorithm, open_text);
    }
}
