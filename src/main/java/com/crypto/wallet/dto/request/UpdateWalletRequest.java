package com.crypto.wallet.dto.request;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class UpdateWalletRequest {

    public enum Operation {
        ADD, SUBTRACT
    }

    private Operation operation;

    private BigDecimal balance;

    public UpdateWalletRequest(Operation operation, BigDecimal balance) {
        this.operation = operation;
        this.balance = balance;
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
}
