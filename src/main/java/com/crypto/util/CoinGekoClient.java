package com.crypto.util;

import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Component
public class CoinGekoClient {

    private final URL url;

    public CoinGekoClient(@Value("api.coingeko.url") String apiUrl) throws MalformedURLException {
        url = new URL(apiUrl);
    }

    public BigDecimal fetchPrice(String crypto){
        HttpURLConnection con = null;
        try{
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(HttpMethod.GET.name());

            int responseCode = 0;
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while (true) {
                    try {
                        if (!((inputLine = in.readLine()) != null)) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    response.append(inputLine);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                BigDecimal price = jsonResponse.getJSONObject(crypto).getBigDecimal("usd");
                return price;


            } else {
                throw new MyServiceException("Failed to fetch prices", ErrorCode.BUSINESS_ERROR);
            }
        }catch (IOException e) {
        throw new MyServiceException("Exception in reading Coin price", ErrorCode.GENERAL, e);
    }
    }
}
