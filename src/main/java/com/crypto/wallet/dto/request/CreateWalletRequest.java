package com.crypto.wallet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema
public class CreateWalletRequest {

    private String userId;
    private String publicKey;
    private String encryptedPrivateKey;

    public CreateWalletRequest(String userId, String publicKey, String encryptedPrivateKey) {
        this.userId = userId;
        this.publicKey = publicKey;
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }
}
