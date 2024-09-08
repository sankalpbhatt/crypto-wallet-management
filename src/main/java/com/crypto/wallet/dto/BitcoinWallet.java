package com.crypto.wallet.dto;

import static com.crypto.wallet.dto.Currency.BITCOIN;

public record BitcoinWallet(String publicKey, String encryptedPrivateKey) implements Wallet {

    @Override
    public Currency getCurrency() {
        return BITCOIN;
    }
}
