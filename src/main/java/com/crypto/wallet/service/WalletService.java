package com.crypto.wallet.service;

import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.request.UpdateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.entity.Wallet;
import org.springframework.transaction.annotation.Transactional;

public interface WalletService {

    WalletResponse createWallet(CreateWalletRequest request);
    WalletResponse getWalletById(String id);
    WalletResponse updateWallet(String id, UpdateWalletRequest updateWalletRequest);
    void deleteWallet(String id);
}
