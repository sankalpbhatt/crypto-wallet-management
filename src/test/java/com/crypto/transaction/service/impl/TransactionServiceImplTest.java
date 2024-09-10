package com.crypto.transaction.service.impl;

import com.crypto.common.entity.SequenceGenerator;
import com.crypto.common.entity.SequenceType;
import com.crypto.common.repository.SequenceGeneratorRepository;
import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;
import com.crypto.transaction.entity.Transaction;
import com.crypto.transaction.entity.TransactionStatus;
import com.crypto.transaction.entity.TransactionType;
import com.crypto.transaction.mapper.TransactionMapper;
import com.crypto.transaction.repository.TransactionRepository;
import com.crypto.transaction.service.TransactionService;
import com.crypto.wallet.dto.Currency;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private TransactionMapper transactionMapper;

    @BeforeEach
    public void setup() {
        transactionService = new TransactionServiceImpl(transactionRepository, sequenceGeneratorRepository, walletService, transactionMapper);
    }

    @Test
    void shouldCreateTransaction() throws InterruptedException {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAmount(BigDecimal.TEN);
        createTransactionRequest.setCurrency(Currency.BITCOIN);
        createTransactionRequest.setType(TransactionType.SEND);
        createTransactionRequest.setWalletId("W001");
        Transaction transaction = new Transaction();
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setBalance(BigDecimal.TEN);
        walletResponse.setCurrency("USD");

        when(sequenceGeneratorRepository.findBySequenceType(anyString()))
                .thenReturn(Optional.of(new SequenceGenerator(SequenceType.WALLET.name(), 1l)));
        when(walletService.getWalletById(anyString())).thenReturn(walletResponse);
        when(transactionRepository.save(any())).thenReturn(transaction);
        when(transactionMapper.mapToResponseDto(any()))
                .thenReturn(new TransactionResponse(
                        "T001", "W001", BigDecimal.TEN, TransactionType.SEND, TransactionStatus.PENDING, null));
        when(transactionMapper.mapToEntity(any(), any())).thenReturn(new Transaction());

        CompletableFuture<TransactionResponse> transactionResponse = transactionService.createTransaction(createTransactionRequest);
        Thread.sleep(5000);
        verify(transactionRepository, times(2)).save(any());
    }
}