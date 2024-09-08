package com.crypto.wallet.dto;

import static com.crypto.wallet.dto.Currency.ETHEREUM;

public record EthereumWallet(String publicKey, String encryptedPrivateKey) implements Wallet {

    @Override
    public Currency getCurrency() {
        return ETHEREUM;
    }
}
