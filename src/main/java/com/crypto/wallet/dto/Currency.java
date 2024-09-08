package com.crypto.wallet.dto;

public enum Currency {

    BITCOIN("Bitcoin"),
    ETHEREUM("Ethereum");

    private String displayName;

    Currency(String displayName) {
        this.displayName = displayName;
    }
}
