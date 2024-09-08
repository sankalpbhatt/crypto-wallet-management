package com.crypto.wallet.dto;

public interface Wallet {

    Currency getCurrency();
    String publicKey();
    String encryptedPrivateKey();
}