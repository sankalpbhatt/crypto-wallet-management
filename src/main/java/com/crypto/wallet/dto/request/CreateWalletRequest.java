package com.crypto.wallet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema
public class CreateWalletRequest {

    private String userId;

    public CreateWalletRequest() {
    }

    public CreateWalletRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
