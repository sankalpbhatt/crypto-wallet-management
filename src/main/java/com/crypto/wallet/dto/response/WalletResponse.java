package com.crypto.wallet.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class WalletResponse {

    private String id;

    @JsonIgnore
    private UUID internalId;
    private UUID userId;
    private Currency currency;
    private BigDecimal balance;

    public WalletResponse() {
    }

    public WalletResponse(String id, UUID userId, Currency currency, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.currency = currency;
        this.balance = balance;
    }

    public WalletResponse(String id, UUID internalId, UUID userId, Currency currency, BigDecimal balance) {
        this.id = id;
        this.internalId = internalId;
        this.userId = userId;
        this.currency = currency;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
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
}
