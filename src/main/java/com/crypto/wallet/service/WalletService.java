package com.crypto.wallet.service;

import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.request.UpdateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;

import java.util.UUID;

public interface WalletService {

    WalletResponse createWallet(CreateWalletRequest request);

    WalletResponse getWalletById(String id);

    WalletResponse getWalletById(UUID id);

    WalletResponse updateWallet(String id, UpdateWalletRequest updateWalletRequest);

    void deleteWallet(String id);
}
