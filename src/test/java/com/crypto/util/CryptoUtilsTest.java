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
        encryptedPrivateKeyString = CryptoUtils.encryptPrivateKey(keyPair.getPrivate(), encryptionPassword);
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
        String publicKeyString = CryptoUtils.getStringPublicKey(keyPair.getPublic());

        // Assert
        assertThat(publicKeyString).isNotNull();
        assertThat(Base64.getDecoder().decode(publicKeyString)).hasSizeGreaterThan(0);
    }

    @Test
    void shouldEncryptDataSuccessfully() {
        // Act
        String encryptedData = CryptoUtils.encryptSignature(testData, keyPair.getPublic());

        // Assert
        assertThat(encryptedData).isNotNull();
        assertThat(Base64.getDecoder().decode(encryptedData)).hasSizeGreaterThan(0);
    }

    @Test
    void shouldThrowExceptionWhenEncryptingWithInvalidKey() {
        // Arrange
        PublicKey invalidKey = null;

        // Act & Assert
        assertThatThrownBy(() -> CryptoUtils.encryptSignature(testData, invalidKey))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Error while encrypting data")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GENERAL);
    }

    @Test
    void shouldEncryptPrivateKeySuccessfully() {
        // Act
        String encryptedPrivateKey = CryptoUtils.encryptPrivateKey(keyPair.getPrivate(), encryptionPassword);

        // Assert
        assertThat(encryptedPrivateKey).isNotNull();
        assertThat(Base64.getDecoder().decode(encryptedPrivateKey)).hasSizeGreaterThan(0);
    }

    @Test
    void shouldDecryptPrivateKeySuccessfully() {
        // Act
        PrivateKey decryptedPrivateKey = CryptoUtils.decryptPrivateKey(encryptedPrivateKeyString, encryptionPassword);

        // Assert
        assertThat(decryptedPrivateKey).isNotNull();
        assertThat(decryptedPrivateKey).isEqualTo(keyPair.getPrivate());
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
        String encryptedData = CryptoUtils.encryptSignature(testData, keyPair.getPublic());

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

    @Test
    void shouldGetPublicKeyFromString() {
        // Arrange
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnZWRKNwDmzLlCnRGSZuoN0QXvgv/u7PmMNF04Y69Wfm61ny8xmxVlau6BR5TszBDU1Mf5WFZd1kGEpueK5goBRtpRxcPeSUbNJSV6s44xFHPnWPVO91srbPnCppKpmzGbYe7GnC1g7ifj0CWROEujupT4dGypUz5hqeOljNoPsQ0yxSXsJVXhyT63bx9I+7VZBNYNvrIgN04L/QRWu6ZqK2wpoiS7TC5U1Uc1nECyUJGd7yx7Lm7jHbtm5ym9IOgz5iWYaMnPuLVt/z3mi7gPc/PIk+edD9HLslN25u3SM4WVhoVw7XEnr6l4d7EMboGDM++mqfkBU3lmlFmN8OJoQIDAQAB";

        // Act
        PublicKey result = CryptoUtils.getPublicKeyFromString(publicKey);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEncoded()).isNotNull();
    }

    @Test
    void shouldGetEncryptedSignature() {
        //Arrange
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnZWRKNwDmzLlCnRGSZuoN0QXvgv/u7PmMNF04Y69Wfm61ny8xmxVlau6BR5TszBDU1Mf5WFZd1kGEpueK5goBRtpRxcPeSUbNJSV6s44xFHPnWPVO91srbPnCppKpmzGbYe7GnC1g7ifj0CWROEujupT4dGypUz5hqeOljNoPsQ0yxSXsJVXhyT63bx9I+7VZBNYNvrIgN04L/QRWu6ZqK2wpoiS7TC5U1Uc1nECyUJGd7yx7Lm7jHbtm5ym9IOgz5iWYaMnPuLVt/z3mi7gPc/PIk+edD9HLslN25u3SM4WVhoVw7XEnr6l4d7EMboGDM++mqfkBU3lmlFmN8OJoQIDAQAB";

        // Act
        String result = CryptoUtils.encryptSignature("Test User", CryptoUtils.getPublicKeyFromString(publicKey));

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void shouldGetDecryptedSignature() {
        //Arrange
        String encryptedSign = "Cpk0lG1aEXwZkGt1Y6JE9MkThrMT1pURCuViwJIPoVBdf8xOvasVmyUitHrLkoody7UzdyBR1BWvd2teqzkpcbkjlc2YI1kXkGa8YDNidIKJoE4eGjBJd9KlAavNDabuxix7V9DH4s+Ts+3QOBP5gyeqJcyWHasIT6l/TclSWU4La1f69pgPizCH6RXJMRGMYg/hO/k9k10vMns05tbQXc65Wpmeu58kiTg+9ew5OZy4bGK3Xfz1fc2p1CEvXFoRwIZCebgcoCqVuCXpVI9FH8B8oqaxLR6CFe8GEXU/mTpbfStnPuDgA8YkDnmdk/gUPVeN/7oii8lrTTfCJMQYNw==";

        PrivateKey privateKey = CryptoUtils.decryptPrivateKey("XrYR/88DfF3ly35idHRBE66PHE/EoUraNOHTeEbNVhO+Pr5jsEReUe8fkAOUHh4X27PaJ/bu2Flw7kY325TTN8eAAF0CH7XMs9xb85GuCT+Z98oiKU1SUopRDzgatpWcKsGzAlAGzoI1Tg3aijjt9DdDyZG2B8+0Nb1go1WlbQozk6g68GO8Qm2sbr6+PxbicEhfYmrJaS0GQxBNtlLMyphvTxHvsN2GET7P6eJnu/OzCykJno8wX1OhOHIjuiXdhV+xBUvIjZjCzrS2/pHJuw6fI8IUTqIiQwOd2beR40oHwjIGVwoQi963tIH0w/Tdm83cqcBmhTP72t8XVT7oPRqKT6muYt4a58TUmVF5CY9R4CT8C2+IaMKYxXgj4ZQK972GfKh5DBJhGKyYaWM3jRjtrYhN9O7YDNKnpcDVs0HnenFhF2cPepaUbEQOrtpszaLipp49MAc+mc7bv24ERj5AkGhYB7wxDfFz8d4P6T8GmV2VtGwva0Q4WqUiXpIxdAevgf5EpNVKY8NJO3v9YzAIJ+Z8RIi6pXozSVQBaS7RxXaSy5+/ZCKSduyWl+n6RflQdmih9+bbF/53M5Xn5BYcNBtT5ZsXg1x71Mc2lwQOrJwBjrzOiSz22oIs219H34u64ur60VpkmmQx5YdLAJG2nBQR+WUbPoQ7TeCap3/QDNMtj8SPSRBs7BPetS/up2/cA/hWm1L4oqAV8GV0Vb1+tQC0AeRTVKUXvdH7AYdPmpFER7Dk47Mf6XicBKtTybcH6r3gpoKzapvQavX+Wt2pJtgo+qgiWQ72SFvGVtA7ZS04n5VrC27PHv/t3RZjNIxwPqQLWK9yVmUtPYgFATmSlAJ0/L0UmxUTNLFoEkwG6Us39ZmVQ0PeToHSX4ukUMb7rW2SoL/6c2FN3BfIS/+H+ZAofRtsm4U2+CjoTg72+qoK6apyRJm47eIBLmP0HTDu2Fh2iw1gu98ysRVob2CBmwUuO+hAMvFGCn7gTOac6RlkSp1DfIqSv9DUR5+A+8bbPSQRharKARocWBMAdLEwPBKsl5/mAvosjgmXEN6cdV/2DUr9Nvl/ShE2iEhB1VgciLq5gCoEXnkmC6be2qT3zQ8SEYq/EJBew7N8sRBk4PSfaPbEV9mbVrBfYA3ENsv15OLFztUpSp+FPzTZLS86H7624rhnqwE9dfSIuPZPKJ9rDeXwq4HCQRf5wKls1ciZW7fYpakp/YK2NnNjQ9d8zLKN/k8EcKXlhw46eX6+RErRGH5bisYm4PfRc56CNH5Lk4lxxEMkHICZaUwO3irYmYobu+RV5w7ZxuoSvnxBOhkUlarLDFO87c+goP++Bfaa487KjoJpY2/kIl891aS0Qm/GCJzrA1cvnjRvOK+3pjcYq0C/OJG4ncJi64PpPtqCTCHpSnFb7P2Z8Nquezi1ItJG2zTEdhx7jNENiBkhGQlSm87yvuV2Eb+9zUiO1Alk1TI6VLwR6OsZNVfeogpBHm0ylduYKSbRICfzjnKpZoxBurhIHhTX7n4j8WRdY7OVONdTJ9emr+VtACiitVPkn4cSul3i6cQ6DG3K58rw/1fq81Sx5sR6tK3wyImd/11w1XkDpOxYWBvj3onf7ieEfH4HJHwctlWf6aZroXI=", "Password");
        String result = CryptoUtils.decryptSignature(encryptedSign, privateKey);

        // Assert
        assertThat(result).isEqualTo("Test User");
    }
}
