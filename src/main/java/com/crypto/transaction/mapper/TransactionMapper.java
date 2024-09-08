package com.crypto.transaction.mapper;

import com.crypto.transaction.dto.response.TransactionResponse;
import com.crypto.transaction.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse mapToResponseDto(Transaction transaction){
        TransactionResponse transactionResponse = null;
        return transactionResponse;
    }
}
