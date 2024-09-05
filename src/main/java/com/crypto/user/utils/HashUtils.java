package com.crypto.user.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashUtils {

    private static final Random random = new Random();
    private static final Integer randomBound = 1501;

    public static String hashPhrase(String phrase) {
        return bytesToHex(convertToHashByte(phrase));
    }

    private static byte[] convertToHashByte(String phrase) {
        MessageDigest digest;
        int hashIterations = random.nextInt(randomBound) + 500;

        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
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
