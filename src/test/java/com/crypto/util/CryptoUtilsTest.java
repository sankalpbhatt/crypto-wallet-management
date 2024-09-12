package com.crypto.util;

import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CryptoUtilsTest {

    private KeyPair keyPair;
    private String testData;
    private String encryptionPassword;
    private String privateKeyString;
    private String encryptedPrivateKeyString;

    @BeforeEach
    void setUp() {
        keyPair = CryptoUtils.generateKeyPair();
        testData = "This is a test.";
        encryptionPassword = "StrongPassword123";
        privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        encryptedPrivateKeyString = CryptoUtils.encryptPrivateKey(privateKeyString, encryptionPassword);
    }

    @Test
    void shouldGenerateKeyPairSuccessfully() {
        // Act
        KeyPair generatedKeyPair = CryptoUtils.generateKeyPair();

        // Assert
        assertThat(generatedKeyPair).isNotNull();
        assertThat(generatedKeyPair.getPrivate()).isNotNull();
        assertThat(generatedKeyPair.getPublic()).isNotNull();
    }

    @Test
    void shouldConvertPublicKeyToStringSuccessfully() {
        // Act
        String publicKeyString = CryptoUtils.convertKeyToString(keyPair.getPublic());

        // Assert
        assertThat(publicKeyString).isNotNull();
        assertThat(Base64.getDecoder().decode(publicKeyString)).hasSizeGreaterThan(0);
    }

    @Test
    void shouldEncryptDataSuccessfully() {
        // Act
        String encryptedData = CryptoUtils.encryptData(testData, keyPair.getPublic());

        // Assert
        assertThat(encryptedData).isNotNull();
        assertThat(Base64.getDecoder().decode(encryptedData)).hasSizeGreaterThan(0);
    }

    @Test
    void shouldThrowExceptionWhenEncryptingWithInvalidKey() {
        // Arrange
        PublicKey invalidKey = null;

        // Act & Assert
        assertThatThrownBy(() -> CryptoUtils.encryptData(testData, invalidKey))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Error while encrypting data")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GENERAL);
    }

    @Test
    void shouldEncryptPrivateKeySuccessfully() {
        // Act
        String encryptedPrivateKey = CryptoUtils.encryptPrivateKey(privateKeyString, encryptionPassword);

        // Assert
        assertThat(encryptedPrivateKey).isNotNull();
        assertThat(Base64.getDecoder().decode(encryptedPrivateKey)).hasSizeGreaterThan(0);
    }

    @Test
    void shouldDecryptPrivateKeySuccessfully() {
        // Act
        String decryptedPrivateKey = CryptoUtils.decryptPrivateKey(encryptedPrivateKeyString, encryptionPassword);

        // Assert
        assertThat(decryptedPrivateKey).isNotNull();
        assertThat(decryptedPrivateKey).isEqualTo(privateKeyString);
    }

    @Test
    void shouldThrowExceptionWhenDecryptingPrivateKeyWithInvalidPassword() {
        // Arrange
        String wrongPassword = "WrongPassword";

        // Act & Assert
        assertThatThrownBy(() -> CryptoUtils.decryptPrivateKey(encryptedPrivateKeyString, wrongPassword))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Error while decrypting private key")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GENERAL);
    }

    @Test
    void shouldDecryptSignatureSuccessfully() {
        // Arrange
        String encryptedData = CryptoUtils.encryptData(testData, keyPair.getPublic());

        // Act
        String decryptedData = CryptoUtils.decryptSignature(encryptedData, keyPair.getPrivate());

        // Assert
        assertThat(decryptedData).isEqualTo(testData);
    }

    @Test
    void shouldGetPrivateKeyFromStringSuccessfully() {
        // Act
        PrivateKey privateKey = CryptoUtils.getPrivateKeyFromString(privateKeyString);

        // Assert
        assertThat(privateKey).isNotNull();
        assertThat(privateKey.getEncoded()).isEqualTo(keyPair.getPrivate().getEncoded());
    }

    @Test
    void shouldThrowExceptionWhenInvalidPrivateKeyStringProvided() {
        // Arrange
        String invalidPrivateKeyString = "InvalidKeyString";

        // Act & Assert
        assertThatThrownBy(() -> CryptoUtils.getPrivateKeyFromString(invalidPrivateKeyString))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Error while generating private key from String")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GENERAL);
    }
}
