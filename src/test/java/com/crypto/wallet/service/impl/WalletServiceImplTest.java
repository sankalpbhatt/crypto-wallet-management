package com.crypto.wallet.service.impl;

import com.crypto.common.entity.SequenceGenerator;
import com.crypto.common.repository.SequenceGeneratorRepository;
import com.crypto.exception.MyServiceException;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.service.UserService;
import com.crypto.util.CoinGekoClient;
import com.crypto.wallet.dto.Currency;
import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.request.UpdateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.entity.Wallet;
import com.crypto.wallet.entity.WalletBalance;
import com.crypto.wallet.mapper.WalletMapper;
import com.crypto.wallet.repository.WalletBalanceRepository;
import com.crypto.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletMapper walletMapper;
    @Mock
    private UserService userService;
    @Mock
    private CoinGekoClient coinGekoClient;
    @Mock
    private WalletBalanceRepository walletBalanceRepository;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;

    private WalletServiceImpl walletService;

    private Wallet wallet;
    private WalletBalance walletBalance;

    @BeforeEach
    public void setUp() {
        walletService = new WalletServiceImpl(
                walletRepository, walletMapper, userService, coinGekoClient, walletBalanceRepository, sequenceGeneratorRepository);
        wallet = new Wallet();
        wallet.setWalletId("WALLET123");
        wallet.setUserId(UUID.randomUUID());
        wallet.setBalances(new ArrayList<>());

        walletBalance = new WalletBalance();
        walletBalance.setCurrency(Currency.BITCOIN);
        walletBalance.setBalance(BigDecimal.valueOf(50000.0));
        walletBalance.setUpdatedAt(LocalDateTime.now());
        wallet.getBalances().add(walletBalance);
    }

    @Test
    public void shouldCreateWallet() {
        CreateWalletRequest request = new CreateWalletRequest("U001");
        UUID internalUserId = UUID.randomUUID();

        WalletResponse expectedResponse = new WalletResponse();
        expectedResponse.setCurrency("USD");
        UserResponse userResponse = new UserResponse("U001",
                internalUserId,
                "Tom",
                "Hardy",
                "9089786756",
                "qwe@gmail.com");
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        sequenceGenerator.setCurrentValue(1l);

        when(userService.getUser(any())).thenReturn(userResponse);
        when(walletMapper.mapRequestToEntity(any(), any())).thenReturn(wallet);
        when(walletRepository.save(any())).thenReturn(wallet);
        when(walletMapper.mapToResponseDto(any(), any())).thenReturn(expectedResponse);
        when(sequenceGeneratorRepository.findBySequenceType(any())).thenReturn(Optional.of(sequenceGenerator));

        WalletResponse response = walletService.createWallet(request);

        assertNotNull(response);
        assertThat(response).usingRecursiveAssertion().isEqualTo(expectedResponse);
        verify(walletRepository).save(wallet);
        verify(userService).getUser("U001");
        verify(walletMapper).mapRequestToEntity(request, internalUserId);
    }

    @Test
    public void shouldGetWalletById() {
        wallet.setBalances(Collections.singletonList(walletBalance));

        when(walletRepository.findByWalletId(any())).thenReturn(Optional.of(wallet));
        when(userService.getUserByInternalId(any(UUID.class))).thenReturn(new UserResponse("U001",
                UUID.randomUUID(),
                "Tom",
                "Hardy",
                "9089786756",
                "qwe@gmail.com"));

        when(coinGekoClient.fetchPrice(anyString())).thenReturn(BigDecimal.valueOf(50000));
        when(walletMapper.mapToResponseDto(any(Wallet.class), anyString())).thenReturn(new WalletResponse());
        WalletResponse response = walletService.getWalletById("WALLET123");

        assertNotNull(response);
        assertThat("USD").isEqualTo(response.getCurrency());
        assertThat(BigDecimal.valueOf(50000).multiply(BigDecimal.valueOf(1.0))).isEqualTo(response.getBalance());
    }

    @Test
    public void shouldThrowExceptionWhenGetWalletById() {
        when(walletRepository.findByWalletId("WALLET123")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> walletService.getWalletById("WALLET123"))
                .isInstanceOf(MyServiceException.class)
                .hasMessage("Business Error : Wallet not found");
    }

    @Test
    public void shouldUpdateWallet() {
        UpdateWalletRequest updateRequest = new UpdateWalletRequest();
        updateRequest.setCurrency(Currency.BITCOIN);
        updateRequest.setBalance(BigDecimal.valueOf(0.5));

        when(walletRepository.findByWalletId(anyString())).thenReturn(Optional.of(wallet));
        when(walletBalanceRepository.save(any())).thenReturn(walletBalance);
        when(walletRepository.save(any())).thenReturn(wallet);
        when(userService.getUserByInternalId(any(UUID.class))).thenReturn(new UserResponse("U001",
                UUID.randomUUID(),
                "Tom",
                "Hardy",
                "9089786756",
                "qwe@gmail.com"));
        when(walletMapper.mapToResponseDto(any(Wallet.class), anyString())).thenReturn(new WalletResponse());
        when(coinGekoClient.fetchPrice(anyString())).thenReturn(new BigDecimal(1.5));
        WalletResponse response = walletService.updateWallet("WALLET123", updateRequest);

        assertNotNull(response);
        assertThat(new BigDecimal(75000.75)).isEqualTo(response.getBalance());
    }

    @Test
    public void shouldUpdateWalletWhenWalletNotFound() {
        UpdateWalletRequest updateRequest = new UpdateWalletRequest();
        updateRequest.setCurrency(Currency.BITCOIN);
        updateRequest.setBalance(BigDecimal.valueOf(0.5));

        when(walletRepository.findByWalletId("WALLET123")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> walletService.updateWallet("WALLET123", updateRequest))
                .isInstanceOf(MyServiceException.class)
                .hasMessage("Business Error : Wallet not found");
    }
}