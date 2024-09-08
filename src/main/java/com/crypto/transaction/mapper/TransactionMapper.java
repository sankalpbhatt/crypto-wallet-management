package com.crypto.transaction.mapper;

import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;
import com.crypto.transaction.entity.Transaction;
import com.crypto.wallet.entity.TransactionStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransactionMapper {

    public TransactionResponse mapToResponseDto(Transaction transaction){
        TransactionResponse transactionResponse = null;
        return transactionResponse;
    }

    public Transaction mapToEntity(CreateTransactionRequest createTransactionRequest, UUID walletId){
        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setTransactionType(createTransactionRequest.getType());
        transaction.setAmount(createTransactionRequest.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setExternalReferenceId(createTransactionRequest.getExternalReferenceId());
        return transaction;
    }
}
