package com.crypto.transaction.service;

import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest);

}