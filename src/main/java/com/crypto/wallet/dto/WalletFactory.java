package com.crypto.wallet.dto;

public class WalletFactory {

    public static Wallet createWallet(Currency currency, String publicKey, String encryptedPrivateKey) {
        return switch (currency) {
            case BITCOIN -> new BitcoinWallet(publicKey, encryptedPrivateKey);
            case ETHEREUM -> new EthereumWallet(publicKey, encryptedPrivateKey);
            default -> throw new IllegalArgumentException("Unsupported currency");
        };
    }
}