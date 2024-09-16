package com.crypto.transaction.mapper;

import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;
import com.crypto.transaction.entity.Transaction;
import com.crypto.transaction.entity.TransactionStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransactionMapper {

    public TransactionResponse mapToResponseDto(Transaction transaction, String walletId) {
        TransactionResponse transactionResponse = new TransactionResponse(transaction.getTransactionId(),
                walletId,
                transaction.getAmount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getTransactionTime());
        return transactionResponse;
    }

    public Transaction mapToEntity(CreateTransactionRequest createTransactionRequest, UUID walletId) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setType(createTransactionRequest.getType());
        transaction.setAmount(createTransactionRequest.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setExternalReferenceId(createTransactionRequest.getExternalReferenceId());
        return transaction;
    }
}
