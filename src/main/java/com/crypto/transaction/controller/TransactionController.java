package com.crypto.transaction.controller;

import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;
import com.crypto.transaction.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public CompletableFuture<TransactionResponse> createTransaction(@RequestBody CreateTransactionRequest createTransactionRequest) {
        return transactionService.createTransaction(createTransactionRequest);
    }

    @GetMapping("{id}")
    public TransactionResponse getTransactionById(@PathVariable("id") String id) {
        return transactionService.getTransactionById(id);
    }
}
