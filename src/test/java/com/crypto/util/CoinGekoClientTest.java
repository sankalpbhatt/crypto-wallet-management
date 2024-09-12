package com.crypto.util;

import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoinGekoClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoinGekoClient coinGekoClient;

    @BeforeEach
    void setUp() {
        coinGekoClient.apiUrl = "http://test.coingecko.com/api/v3/simple/price";
    }

    @Test
    void shouldReturnPriceSuccessfully() {
        // Arrange
        String crypto = "bitcoin";
        Map<String, Map<String, Object>> responseBody = new HashMap<>();
        Map<String, Object> cryptoData = new HashMap<>();
        cryptoData.put("usd", 45000.50);
        responseBody.put(crypto, cryptoData);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.getForEntity(coinGekoClient.apiUrl, Map.class))
                .thenReturn(responseEntity);

        // Act
        BigDecimal price = coinGekoClient.fetchPrice(crypto);

        // Assert
        assertThat(price).isNotNull();
        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(45000.50));
    }

    @Test
    void shouldThrowExceptionWhenCryptoNotFound() {
        // Arrange
        String crypto = "unknown-crypto";
        Map<String, Map<String, Object>> responseBody = new HashMap<>();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.getForEntity(coinGekoClient.apiUrl, Map.class))
                .thenReturn(responseEntity);

        // Act & Assert
        assertThatThrownBy(() -> coinGekoClient.fetchPrice(crypto))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Cryptocurrency not found in the response: " + crypto)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GENERAL);
    }

    @Test
    void shouldThrowExceptionWhenResponseIsNotOk() {
        // Arrange
        String crypto = "bitcoin";
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        when(restTemplate.getForEntity(coinGekoClient.apiUrl, Map.class)).thenReturn(responseEntity);

        // Act & Assert
        assertThatThrownBy(() -> coinGekoClient.fetchPrice(crypto))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Failed to fetch crypto prices. Status code: " + HttpStatus.BAD_REQUEST)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GENERAL);
    }

    @Test
    void shouldThrowExceptionWhenRestClientExceptionOccurs() {
        // Arrange
        String crypto = "bitcoin";
        when(restTemplate.getForEntity(coinGekoClient.apiUrl, Map.class))
                .thenThrow(new RestClientException("API error"));

        // Act & Assert
        assertThatThrownBy(() -> coinGekoClient.fetchPrice(crypto))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Error occurred while fetching prices: API error")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.GENERAL);
    }
}
