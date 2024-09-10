package com.crypto.transaction.controller;

import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;
import com.crypto.transaction.entity.TransactionStatus;
import com.crypto.transaction.entity.TransactionType;
import com.crypto.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;
    private TransactionController transactionController;

    @BeforeEach
    public void setup() {
        transactionController = new TransactionController(transactionService);
    }

    @Test
    void shouldCreateTransaction() {
        CreateTransactionRequest createTransactionRequest = Mockito.mock(CreateTransactionRequest.class);
        TransactionResponse response = new TransactionResponse("T001",
                "1231424",
                BigDecimal.ONE,
                TransactionType.SEND,
                TransactionStatus.CONFIRMED,
                null);
        when(transactionService.createTransaction(any())).thenReturn(response);
        TransactionResponse actualResponse = transactionController.createTransaction(createTransactionRequest);
        verify(transactionService).createTransaction(createTransactionRequest);
        assertThat(actualResponse).isEqualTo(response);
    }

    @Test
    void shouldThrowExceptionWhenServiceThrowsExceptionWhenCreatingTransaction() {
        CreateTransactionRequest createTransactionRequest = Mockito.mock(CreateTransactionRequest.class);
        doThrow(new MyServiceException("New Error", ErrorCode.BUSINESS_ERROR))
                .when(transactionService).createTransaction(any());
        assertThatThrownBy(() -> transactionController.createTransaction(createTransactionRequest))
                .isInstanceOf(MyServiceException.class)
                .hasMessage(String.format(ErrorCode.BUSINESS_ERROR.getMessage(), "New Error"));
        verify(transactionService).createTransaction(createTransactionRequest);
    }

    @Test
    void shouldGetTransactionById() {
        TransactionResponse response = new TransactionResponse("T001",
                "1231424",
                BigDecimal.ONE,
                TransactionType.SEND,
                TransactionStatus.CONFIRMED,
                null);
        when(transactionService.getTransactionById(any())).thenReturn(response);
        TransactionResponse actualResponse = transactionController.getTransactionById("T001");
        verify(transactionService).getTransactionById("T001");
        assertThat(actualResponse).isEqualTo(response);
    }

    @Test
    void shouldThrowExceptionWhenServiceThrowsExceptionWhenGettingTransaction() {
        doThrow(new MyServiceException("New Error", ErrorCode.BUSINESS_ERROR))
                .when(transactionService).getTransactionById(anyString());
        assertThatThrownBy(() -> transactionController.getTransactionById("T001"))
                .isInstanceOf(MyServiceException.class)
                .hasMessage(String.format(ErrorCode.BUSINESS_ERROR.getMessage(), "New Error"));
        verify(transactionService).getTransactionById("T001");
    }
}