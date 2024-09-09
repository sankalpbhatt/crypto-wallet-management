package com.crypto.wallet.mapper;

import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class WalletMapper {

    public WalletResponse mapToResponseDto(Wallet wallet, String userId){
        return new WalletResponse(
                wallet.getWalletId(),
                userId,
                wallet.getCurrency(),
                wallet.getBalance()
        );
    }

    public Wallet mapRequestToEntity(CreateWalletRequest request, UUID userId){
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setEncryptedPrivateKey(request.getEncryptedPrivateKey());
        wallet.setPublicKey(request.getPublicKey());
        wallet.setCurrency(request.getCurrency());
        return wallet;
    }
}
