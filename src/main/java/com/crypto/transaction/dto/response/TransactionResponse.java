package com.crypto.transaction.dto.response;

import com.crypto.transaction.entity.TransactionStatus;
import com.crypto.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private String transactionId;
    private String walletId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime timestamp;

    public TransactionResponse(String transactionId,
                               String walletId,
                               BigDecimal amount,
                               TransactionType transactionType,
                               TransactionStatus status,
                               LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.timestamp = timestamp;
    }

    public TransactionResponse(String transactionId,
                               BigDecimal amount,
                               TransactionType transactionType,
                               TransactionStatus status,
                               LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
