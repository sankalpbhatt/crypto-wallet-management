package com.crypto.transaction.service.impl;

import com.crypto.common.entity.SequenceGenerator;
import com.crypto.common.entity.SequenceType;
import com.crypto.common.repository.SequenceGeneratorRepository;
import com.crypto.exception.MyServiceException;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        // Arrange
        UUID internalId = UUID.randomUUID();
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId("W001");
        request.setAmount(new BigDecimal(100));
        request.setType(TransactionType.SEND);
        request.setCurrency(Currency.ETHEREUM);
        request.setSignature("signature");

        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setId("W001");
        walletResponse.setInternalId(internalId);
        walletResponse.setBalance(new BigDecimal(200));
        walletResponse.setEncryptedKey("encryptedKey");

        Transaction transaction = new Transaction();
        transaction.setTransactionId("T001");

        when(walletService.getWalletById(request.getWalletId())).thenReturn(walletResponse);
        when(transactionMapper.mapToEntity(request, walletResponse.getInternalId())).thenReturn(transaction);
        when(sequenceGeneratorRepository.findBySequenceType(anyString()))
                .thenReturn(Optional.of(new SequenceGenerator(SequenceType.TRANSACTION.name(), 1L)));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.mapToResponseDto(any()))
                .thenReturn(new TransactionResponse("T001", "W001",
                        new BigDecimal(200), TransactionType.SEND, TransactionStatus.CONFIRMED, null));

        // Act
        CompletableFuture<TransactionResponse> futureResponse = transactionService.createTransaction(request);
        TransactionResponse response = futureResponse.join();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getWalletId()).isEqualTo(walletResponse.getId());
        assertThat(response.getStatus()).isEqualTo(TransactionStatus.CONFIRMED);
    }

    @Test
    void shouldThrowExceptionForInsufficientFunds() {
        // Arrange
        UUID internalId = UUID.randomUUID();
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId("W001");
        request.setAmount(new BigDecimal(300));
        request.setType(TransactionType.SEND);
        request.setCurrency(Currency.ETHEREUM);

        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setId("W001");
        walletResponse.setInternalId(internalId);
        walletResponse.setBalance(new BigDecimal(200));

        when(walletService.getWalletById(request.getWalletId())).thenReturn(walletResponse);

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(request))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Insufficient Funds");
    }
}