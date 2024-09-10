package com.crypto.wallet.dto.response;

import com.crypto.wallet.dto.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class WalletResponse {

    private String id;

    @JsonIgnore
    private UUID internalId;
    private String userId;
    private Currency currency;
    private BigDecimal balance;
    private Map<String, BigDecimal> balances;

    public WalletResponse() {
    }

    public WalletResponse(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public WalletResponse(String id, Currency currency, BigDecimal balance) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
    }

    public WalletResponse(String id, UUID internalId, String userId, Currency currency, BigDecimal balance) {
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
}
