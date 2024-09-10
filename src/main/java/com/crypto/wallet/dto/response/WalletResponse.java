package com.crypto.wallet.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class WalletResponse {

    private String id;

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
}
