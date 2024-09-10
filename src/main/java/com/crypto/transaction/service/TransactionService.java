package com.crypto.transaction.service;

import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {

    CompletableFuture<TransactionResponse> createTransaction(CreateTransactionRequest createTransactionRequest);

    TransactionResponse getTransactionById(String id);
}