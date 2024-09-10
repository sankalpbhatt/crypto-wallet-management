package com.crypto.wallet.mapper;

import com.crypto.util.CoinGekoClient;
import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.entity.Wallet;
import com.crypto.wallet.entity.WalletBalance;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Component
public class WalletMapper {

    public WalletResponse mapToResponseDto(Wallet wallet, String userId){
        return new WalletResponse(
                wallet.getWalletId(),
                userId
        );
    }

    public Wallet mapRequestToEntity(CreateWalletRequest request, UUID userId){
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setEncryptedPrivateKey(request.getEncryptedPrivateKey());
        wallet.setPublicKey(request.getPublicKey());
        return wallet;
    }
}
