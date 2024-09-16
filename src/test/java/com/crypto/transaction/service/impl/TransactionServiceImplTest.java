package com.crypto.transaction.service.impl;

import com.crypto.common.repository.SequenceGeneratorRepository;
import com.crypto.exception.MyServiceException;
import com.crypto.transaction.dto.request.CreateTransactionRequest;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
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
    void shouldThrowExceptionWithErrorNoBalanceForCurrencyCreateTransaction() {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAmount(BigDecimal.TEN);
        createTransactionRequest.setCurrency(Currency.BITCOIN);
        createTransactionRequest.setType(TransactionType.SEND);
        createTransactionRequest.setWalletId("W001");

        Map<String, BigDecimal> balances = new HashMap<>();
        balances.put(Currency.ETHEREUM.name(), new BigDecimal(200));
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setBalance(BigDecimal.TEN);
        walletResponse.setCurrency("USD");
        walletResponse.setBalances(balances);

        when(walletService.getWalletById(anyString())).thenReturn(walletResponse);

        assertThatThrownBy(() -> transactionService.createTransaction(createTransactionRequest))
                .isInstanceOf(MyServiceException.class)
                .hasMessage("Business Error : No funds with currency: BITCOIN");
    }

    @Test
    void shouldThrowExceptionForInsufficientBalance() {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAmount(BigDecimal.TEN);
        createTransactionRequest.setCurrency(Currency.BITCOIN);
        createTransactionRequest.setType(TransactionType.SEND);
        createTransactionRequest.setWalletId("W001");

        Map<String, BigDecimal> balances = new HashMap<>();
        balances.put(Currency.BITCOIN.name(), new BigDecimal(5));
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setBalance(BigDecimal.TEN);
        walletResponse.setCurrency("USD");
        walletResponse.setBalances(balances);

        when(walletService.getWalletById(anyString())).thenReturn(walletResponse);

        assertThatThrownBy(() -> transactionService.createTransaction(createTransactionRequest))
                .isInstanceOf(MyServiceException.class)
                .hasMessage("Business Error : Insufficient Funds");
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

        Map<String, BigDecimal> balances = new HashMap<>();
        balances.put(Currency.ETHEREUM.name(), new BigDecimal(200));
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setId("W001");
        walletResponse.setInternalId(internalId);
        walletResponse.setBalance(new BigDecimal(200));
        walletResponse.setBalances(balances);

        when(walletService.getWalletById(request.getWalletId())).thenReturn(walletResponse);

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(request))
                .isInstanceOf(MyServiceException.class)
                .hasMessageContaining("Insufficient Funds");
    }
}