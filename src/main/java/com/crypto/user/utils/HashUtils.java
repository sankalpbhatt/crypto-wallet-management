package com.crypto.user.utils;

import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashUtils {

    public static String hashPhrase(String phrase, int hashIterations) {
        return bytesToHex(convertToHashByte(phrase, hashIterations));
    }

    private static byte[] convertToHashByte(String phrase, int hashIterations) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new MyServiceException("Error while creating Message digest", ErrorCode.GENERAL, e);
        }
        byte[] hash = phrase.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < hashIterations; i++) {
            hash = digest.digest(hash);
        }
        return hash;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
