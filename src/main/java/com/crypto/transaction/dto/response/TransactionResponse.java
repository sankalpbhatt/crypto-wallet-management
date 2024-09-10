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
}
