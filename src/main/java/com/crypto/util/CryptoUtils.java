package com.crypto.util;

import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptoUtils {
    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    public static KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new MyServiceException("Error while getting KeyPair", ErrorCode.GENERAL, e);
        }
        keyPairGenerator.initialize(2048); // Use 2048 bits key size
        return keyPairGenerator.generateKeyPair();
    }

    public static String convertKeyToString(PublicKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String encryptSignature(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchPaddingException e) {
            throw new MyServiceException("Error while encrypting data", ErrorCode.GENERAL, e);
        } catch (IllegalBlockSizeException e) {
            throw new MyServiceException("Error while encrypting data", ErrorCode.GENERAL, e);
        } catch (NoSuchAlgorithmException e) {
            throw new MyServiceException("Error while encrypting data", ErrorCode.GENERAL, e);
        } catch (BadPaddingException e) {
            throw new MyServiceException("Error while encrypting data", ErrorCode.GENERAL, e);
        } catch (InvalidKeyException e) {
            throw new MyServiceException("Error while encrypting data", ErrorCode.GENERAL, e);
        }
    }

    public static String encryptPrivateKey(PrivateKey privateKey, String encryptionPassword) {
        try {
            // Create SecretKey from password
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(encryptionPassword.toCharArray(), "salt".getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Encrypt private key bytes directly
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(privateKey.getEncoded());

            // Return encrypted private key as a Base64 encoded string
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new MyServiceException("Error while encrypting private key", ErrorCode.GENERAL, e);
        }
    }

    public static PrivateKey decryptPrivateKey(String encryptedPrivateKeyStr, String encryptionPassword) {
        try {
            // Create SecretKey from password
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(encryptionPassword.toCharArray(), "salt".getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Decrypt the private key bytes
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPrivateKeyStr));

            // Rebuild the PrivateKey object from the decrypted bytes
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decryptedBytes);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new MyServiceException("Error while decrypting private key", ErrorCode.GENERAL, e);
        }
    }

    public static String decryptSignature(String encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error while verifying signature", e);
        }
    }

    public static PrivateKey getPrivateKeyFromString(String privateKeyStr) {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new MyServiceException("Error while getting KeyInstance", ErrorCode.GENERAL, e);
        }
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new MyServiceException("Error while generating private key from String", ErrorCode.GENERAL, e);
        }
    }

    public static PublicKey getPublicKeyFromString(String publicKeyStr) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);

            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new MyServiceException("Error while converting string to PublicKey", ErrorCode.GENERAL, e);
        }
    }
}

