package com.crypto.common.entity;

public enum SequenceType {

    USER,
    WALLET,
    TRANSACTION;

    public String getPrefix() {
        return name().substring(0, 1);
    }
}