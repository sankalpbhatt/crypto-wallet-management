package com.crypto.transaction.controller;

import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.entity.TransactionType;
import com.crypto.user.dto.CreateUserRequest;
import com.crypto.wallet.dto.Currency;
import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.request.UpdateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String authToken;
    private String walletId;


    @BeforeEach
    void setUp() throws Exception {
        String randomEmail = "user" + UUID.randomUUID().toString() + "@example.com";

        this.authToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJjbGllbnRJZCI6InlvdXJfY2xpZW50X2lkIn0.bPuQxUr8eICFhU2cxbB2FLKzFwyy6cwbhcwPiUVEa8D5Al8_N2nIB1STgIIAbnPDaVULSw44ZpewfQc_z9J3eM8UiCA_gvHpaFHB6IDv41yOL6z-NhNRJU6vry-oM8Q5e7_rkpvLyeCvpQsbsZNCVaz8szlHyPNunYMxkVZNW03XHMWok2Zn4QwXXqkC4nBop3MkZ3AL8oi8GFwsieGu81a0U2DYgyXQyfN4oTG0NbOEXGlLrwI8vElGEPb71d1mjWFuio0UestLgCzir0o39gxdEQWaI7OCTDQwyjYIEOd5l1HQE3qoI_JAKX1Z0892Gk_JC9urUU8glP1bxCp4PA";
        CreateUserRequest createUserRequest = new CreateUserRequest(
                "firstName",
                "lastName",
                "phone",
                randomEmail,
                "password"
        );

        String userRequestBody = objectMapper.writeValueAsString(createUserRequest);

        WalletResponse userResponse = objectMapper.readValue(
                mockMvc.perform(post("/user")
                                .header(HttpHeaders.AUTHORIZATION, authToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequestBody))
                        .andExpect(status().is(HttpStatus.CREATED.value()))
                        .andReturn().getResponse().getContentAsString(),
                WalletResponse.class
        );

        CreateWalletRequest createWalletRequest = new CreateWalletRequest(userResponse.getId());

        String walletRequestBody = objectMapper.writeValueAsString(createWalletRequest);

        WalletResponse walletResponse = objectMapper.readValue(
                mockMvc.perform(post("/wallet")
                                .header(HttpHeaders.AUTHORIZATION, authToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(walletRequestBody))
                        .andExpect(status().is(HttpStatus.CREATED.value()))
                        .andReturn().getResponse().getContentAsString(),
                WalletResponse.class
        );

        this.walletId = walletResponse.getId();

        UpdateWalletRequest updateWalletRequest = new UpdateWalletRequest(
                UpdateWalletRequest.Operation.ADD, new BigDecimal(10), Currency.ETHEREUM);
        String updateWalletRequestBody = objectMapper.writeValueAsString(updateWalletRequest);

        mockMvc.perform(put("/wallet/{id}", this.walletId)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateWalletRequestBody))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId(this.walletId);
        request.setAmount(BigDecimal.valueOf(100));
        request.setType(TransactionType.RECEIVE);
        request.setCurrency(Currency.ETHEREUM);
        request.setSignature("signature");
        request.setExternalReferenceId(UUID.randomUUID().toString());

        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/transaction")
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }
}
