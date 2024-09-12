package com.crypto.wallet.controller;

import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    private WalletController walletController;

    @Mock
    private WalletService walletService;

    WalletControllerTest() {
    }

    @BeforeEach
    public void setup() {
        walletController = new WalletController(walletService);
    }

    @Test
    void shouldCreateWallet() {
        CreateWalletRequest request = new CreateWalletRequest("U001");
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setCurrency("USD");
        walletResponse.setId("W001");
        walletResponse.setBalance(BigDecimal.TEN);
        walletResponse.setUserId("U001");
        when(walletService.createWallet(any())).thenReturn(walletResponse);
        WalletResponse actualWalletResponse = walletController.createWallet(request);
        assertThat(actualWalletResponse).usingRecursiveAssertion().isEqualTo(walletResponse);
        ArgumentCaptor<CreateWalletRequest> createWalletRequestArgumentCaptor = ArgumentCaptor.captor();
        verify(walletService).createWallet(createWalletRequestArgumentCaptor.capture());
        assertThat(createWalletRequestArgumentCaptor.getValue()).usingRecursiveAssertion().isEqualTo(request);
    }
}