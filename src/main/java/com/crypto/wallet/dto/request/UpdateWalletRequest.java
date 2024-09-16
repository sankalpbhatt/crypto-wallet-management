package com.crypto.wallet.dto.request;

import com.crypto.wallet.dto.Currency;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class UpdateWalletRequest {

    public enum Operation {
        ADD, SUBTRACT
    }

    @NotNull
    private Operation operation;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private Currency currency;

    public UpdateWalletRequest() {
    }

    public UpdateWalletRequest(Operation operation, BigDecimal balance) {
        this.operation = operation;
        this.balance = balance;
    }

    public UpdateWalletRequest(Operation operation, BigDecimal balance, Currency currency) {
        this.operation = operation;
        this.balance = balance;
        this.currency = currency;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
