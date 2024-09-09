package com.crypto.wallet.dto;

import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;

public class WalletFactory {

    public static Wallet createWallet(Currency currency, String publicKey, String encryptedPrivateKey) {
        return switch (currency) {
            case BITCOIN -> new BitcoinWallet(publicKey, encryptedPrivateKey);
            case ETHEREUM -> new EthereumWallet(publicKey, encryptedPrivateKey);
            default -> throw new MyServiceException("Unsupported currency", ErrorCode.BUSINESS_ERROR);
        };
    }
}