package com.crypto.wallet.mapper;

import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.entity.Wallet;
import com.crypto.wallet.entity.WalletBalance;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WalletMapper {

    public WalletResponse mapToResponseDto(Wallet wallet, String userId) {
        WalletResponse walletResponse = new WalletResponse(
                wallet.getWalletId(),
                userId
        );
        walletResponse.setBalances(mapBalances(wallet.getBalances()));
        return walletResponse;
    }

    public Map<String, BigDecimal> mapBalances(List<WalletBalance> walletBalances) {
        return walletBalances.stream()
                .collect(Collectors.toMap(
                        balance -> balance.getCurrency().name(),
                        balance -> balance.getBalance()
                ));
    }

    public Wallet mapRequestToEntity(CreateWalletRequest request, UUID userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setEncryptedPrivateKey("encrypted key");
        wallet.setPublicKey("public key");
        return wallet;
    }
}
