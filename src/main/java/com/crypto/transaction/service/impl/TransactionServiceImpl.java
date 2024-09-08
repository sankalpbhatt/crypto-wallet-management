package com.crypto.transaction.service.impl;

import com.crypto.common.service.impl.SequenceGeneratorServiceImpl;
import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;
import com.crypto.transaction.repository.TransactionRepository;
import com.crypto.transaction.service.TransactionService;

public class TransactionServiceImpl extends SequenceGeneratorServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest) {
        return null;
    }
}
