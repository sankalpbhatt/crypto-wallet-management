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

    public static String encryptData(String data, PublicKey publicKey) {
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

    public static String convertKeyToString(PrivateKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String encryptPrivateKey(String privateKeyStr, String encryptionPassword) {
        try {
            SecretKeyFactory factory;

            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(encryptionPassword.toCharArray(), "salt".getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] encryptedBytes = cipher.doFinal(Base64.getDecoder().decode(privateKeyStr));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (RuntimeException | InvalidKeyException e) {
            throw new MyServiceException("Error while encrypting private key", ErrorCode.GENERAL, e);
        } catch (NoSuchPaddingException e) {
            throw new MyServiceException("Error while encrypting private key", ErrorCode.GENERAL, e);
        } catch (IllegalBlockSizeException e) {
            throw new MyServiceException("Error while encrypting private key", ErrorCode.GENERAL, e);
        } catch (NoSuchAlgorithmException e) {
            throw new MyServiceException("Error while encrypting private key", ErrorCode.GENERAL, e);
        } catch (InvalidKeySpecException e) {
            throw new MyServiceException("Error while encrypting private key", ErrorCode.GENERAL, e);
        } catch (BadPaddingException e) {
            throw new MyServiceException("Error while encrypting private key", ErrorCode.GENERAL, e);
        }
    }

    public static String decryptPrivateKey(String encryptedPrivateKeyStr, String encryptionPassword) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(encryptionPassword.toCharArray(), "salt".getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPrivateKeyStr));
            return Base64.getEncoder().encodeToString(decryptedBytes);
        } catch (RuntimeException | InvalidKeyException e) {
            throw new MyServiceException("Error while decrypting private key", ErrorCode.GENERAL, e);
        } catch (NoSuchPaddingException e) {
            throw new MyServiceException("Error while decrypting private key", ErrorCode.GENERAL, e);
        } catch (IllegalBlockSizeException e) {
            throw new MyServiceException("Error while decrypting private key", ErrorCode.GENERAL, e);
        } catch (NoSuchAlgorithmException e) {
            throw new MyServiceException("Error while decrypting private key", ErrorCode.GENERAL, e);
        } catch (InvalidKeySpecException e) {
            throw new MyServiceException("Error while decrypting private key", ErrorCode.GENERAL, e);
        } catch (BadPaddingException e) {
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
}

