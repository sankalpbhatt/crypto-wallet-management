package com.crypto.transaction.dto.response;

import com.crypto.wallet.entity.TransactionStatus;
import com.crypto.wallet.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TransactionResponse {

    private String transactionId;
    private UUID walletId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime timestamp;

    public TransactionResponse(String transactionId,
                               UUID walletId,
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
