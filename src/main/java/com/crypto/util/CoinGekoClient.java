package com.crypto.util;

import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Map;

@Component
public class CoinGekoClient {

    private final RestTemplate restTemplate;
    @Value("${api.coingecko.url}")
    String apiUrl;

    public CoinGekoClient(RestTemplate restTemplate) throws MalformedURLException {
        this.restTemplate = restTemplate;
    }

    public BigDecimal fetchPrice(String crypto) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Map<String, Object>> jsonResponse = response.getBody();
                if (jsonResponse != null && jsonResponse.containsKey(crypto)) {
                    Map<String, Object> cryptoData = jsonResponse.get(crypto);
                    return new BigDecimal(cryptoData.get("usd").toString());
                } else {
                    throw new MyServiceException(
                            "Cryptocurrency not found in the response: " + crypto, ErrorCode.GENERAL);
                }
            } else {
                throw new MyServiceException(
                        "Failed to fetch crypto prices. Status code: " + response.getStatusCode(), ErrorCode.GENERAL);
            }
        } catch (RestClientException e) {
            throw new MyServiceException(
                    "Error occurred while fetching prices: " + e.getMessage(), ErrorCode.GENERAL, e);
        }
    }
}
