package com.crypto.wallet.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class WalletResponse {

    private String id;

    @JsonIgnore
    private String encryptedKey;

    @JsonIgnore
    private String publicKey;

    @JsonIgnore
    private UUID internalId;
    private String userId;
    private String currency;
    private BigDecimal balance;
    private Map<String, BigDecimal> balances;

    public WalletResponse() {
    }

    public WalletResponse(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public WalletResponse(String id, String userId, String encryptedKey, String publicKey) {
        this.id = id;
        this.userId = userId;
        this.encryptedKey = encryptedKey;
        this.publicKey = publicKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UUID getInternalId() {
        return internalId;
    }

    public void setInternalId(UUID internalId) {
        this.internalId = internalId;
    }

    public Map<String, BigDecimal> getBalances() {
        return balances;
    }

    public void setBalances(Map<String, BigDecimal> balances) {
        this.balances = balances;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
