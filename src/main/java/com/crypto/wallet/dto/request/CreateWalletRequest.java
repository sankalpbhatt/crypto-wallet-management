package com.crypto.wallet.dto.request;

import com.crypto.wallet.dto.Currency;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema
public class CreateWalletRequest {

    private String userId;
    private Currency currency;
    private String publicKey;
    private String encryptedPrivateKey;

    public CreateWalletRequest(String userId, Currency currency, String publicKey, String encryptedPrivateKey) {
        this.userId = userId;
        this.currency = currency;
        this.publicKey = publicKey;
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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
