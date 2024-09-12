package com.crypto.user.controller;

import com.crypto.user.dto.CreateUserRequest;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String authToken;


    @BeforeEach
    void setUp() {
        this.authToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJjbGllbnRJZCI6InlvdXJfY2xpZW50X2lkIn0.bPuQxUr8eICFhU2cxbB2FLKzFwyy6cwbhcwPiUVEa8D5Al8_N2nIB1STgIIAbnPDaVULSw44ZpewfQc_z9J3eM8UiCA_gvHpaFHB6IDv41yOL6z-NhNRJU6vry-oM8Q5e7_rkpvLyeCvpQsbsZNCVaz8szlHyPNunYMxkVZNW03XHMWok2Zn4QwXXqkC4nBop3MkZ3AL8oi8GFwsieGu81a0U2DYgyXQyfN4oTG0NbOEXGlLrwI8vElGEPb71d1mjWFuio0UestLgCzir0o39gxdEQWaI7OCTDQwyjYIEOd5l1HQE3qoI_JAKX1Z0892Gk_JC9urUU8glP1bxCp4PA";
    }

    @Test
    void shouldCreateUser() throws Exception {
        // Arrange
        String randomEmail = "user" + UUID.randomUUID().toString() + "@example.com";
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
    }
}
